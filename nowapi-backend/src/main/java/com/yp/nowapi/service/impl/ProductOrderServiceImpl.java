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
import com.yp.nowapi.model.entity.PaymentStatusEnum;
import com.yp.nowapi.model.entity.ProductInfo;
import com.yp.nowapi.model.entity.ProductOrder;
import com.yp.nowapi.model.entity.User;
import com.yp.nowapi.payment.AliPayBean;
import com.yp.nowapi.service.ProductOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
        lqw.eq(ProductOrder::getStatus, PaymentStatusEnum.NOTPAY.getValue());

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
        order.setStatus(PaymentStatusEnum.NOTPAY.getValue());
        // 4. 保存订单
        productOrderMapper.insert(order);

        return order;
    }
}




