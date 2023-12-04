package com.yp.nowapi.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ijpay.alipay.AliPayApi;
import com.yp.nowapi.common.ErrorCode;
import com.yp.nowapi.exception.BusinessException;
import com.yp.nowapi.manager.CosManager;
import com.yp.nowapi.mapper.ProductInfoMapper;
import com.yp.nowapi.mapper.ProductOrderMapper;
import com.yp.nowapi.model.entity.ProductInfo;
import com.yp.nowapi.model.entity.ProductOrder;
import com.yp.nowapi.model.entity.User;
import com.yp.nowapi.payment.AliPayBean;
import com.yp.nowapi.service.ProductOrderService;
import com.yp.nowapi.state.OrderState;
import com.yp.nowapi.state.OrderStateChangeAction;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class ProductOrderServiceImpl extends ServiceImpl<ProductOrderMapper, ProductOrder>
        implements ProductOrderService {

    @Resource
    private ProductInfoMapper productInfoMapper;

    @Resource
    private ProductOrderMapper productOrderMapper;

    @Resource
    private AliPayBean aliPayBean;

    @Resource
    private CosManager cosManager;

    @Resource
    private StateMachine<OrderState, OrderStateChangeAction> ordersStateMachine;

    @Resource
    private StateMachinePersister<OrderState, OrderStateChangeAction, String> stateMachineRedisPersister;

    @Resource
    private RedisTemplate redisTemplate;


    @Override
    public ProductOrder createOrder(Long productId, User loginUser) {
        ProductInfo productInfo = productInfoMapper.selectById(productId);
        if (productInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        Long price = productInfo.getPrice();
        String name = productInfo.getName();

        // 查询是否存在订单
        LambdaQueryWrapper<ProductOrder> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ProductOrder::getProductId, productId);
        lqw.eq(ProductOrder::getUserId, loginUser.getId());
        lqw.eq(ProductOrder::getStatus, OrderState.ORDER_WAIT_PAY);

        ProductOrder productOrder = productOrderMapper.selectOne(lqw);
        if (productOrder != null) {
            return productOrder;
        }

        // 调用支付宝的接口生成二维码
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setSubject(name);
        model.setTotalAmount(String.valueOf(price));
        model.setTimeoutExpress("5m");
        model.setOutTradeNo("12345678");
        try {
            // todo 设置回调地址
            String resultStr = AliPayApi.tradePrecreatePayToResponse(model, "回调地址").getBody();
            JSONObject jsonObject = JSONObject.parseObject(resultStr);
            // todo 创建输出流并保存到cos
//            QrCodeUtil.generate(jsonObject.getJSONObject("alipay_trade_precreate_response").getString("qr_code"), 300, 300, ImgUtil.IMAGE_TYPE_PNG, );
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 3. 生成订单
        ProductOrder order = new ProductOrder();
        order.setUserId(loginUser.getId());
        order.setProductId(productId);
        order.setOrderName(name);
        order.setTotal(price);
        order.setStatus(OrderState.ORDER_WAIT_PAY);
        // 4. 保存订单
        productOrderMapper.insert(order);

        // todo 将订单放入Redis缓存
        redisTemplate.opsForValue().set("product_order." + order.getId(), order, 15, TimeUnit.MINUTES);
        return order;
    }

    @Override
    public ProductOrder pay(Long id) {
        // 1. 从Redis缓存中取数据
        ProductOrder productOrder = (ProductOrder) redisTemplate.opsForValue().get(id);
        if (productOrder == null) {
            productOrder = productOrderMapper.selectById(id);
        }

        // 2. 包装状态变更Message，并附带订单操作PAY_ORDER
        Message<OrderStateChangeAction> message = MessageBuilder.withPayload(OrderStateChangeAction.PAY_ORDER).setHeader("productOrder", productOrder).build();
        if (changeStateAction(message, productOrder)) {
            return productOrder;
        }
        return null;
    }


    private boolean changeStateAction(Message<OrderStateChangeAction> message, ProductOrder productOrder) {
        try {
            //启动状态机
            ordersStateMachine.start();
            //从Redis缓存中读取状态机，缓存的Key为orderId+"STATE"，这是自定义的
            stateMachineRedisPersister.restore(ordersStateMachine, productOrder.getId() + "STATE");
            //将Message发送给OrderStateListener
            boolean res = ordersStateMachine.sendEvent(message);
            //将更改完订单状态的 状态机 存储到 Redis缓存
            stateMachineRedisPersister.persist(ordersStateMachine, productOrder.getId() + "STATE");
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ordersStateMachine.stop();
        }
        return false;
    }
}




