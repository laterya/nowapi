package com.yp.nowapi.state;

import com.yp.nowapi.common.ErrorCode;
import com.yp.nowapi.exception.BusinessException;
import com.yp.nowapi.exception.ThrowUtils;
import com.yp.nowapi.model.entity.ProductOrder;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

/**
 * @author yp
 * @date: 2023/11/17
 */
@Component
@WithStateMachine(name = "orderStateMachine")
public class OrderStateListener {


    @OnTransition(source = "ORDER_WAIT_PAY", target = "ORDER_PAY_FINISH")
    public boolean payToFinish(Message<OrderStateChangeAction> message) {
        ProductOrder productOrder = (ProductOrder) message.getHeaders().get("productOrder");
        ThrowUtils.throwIf(productOrder == null, ErrorCode.SYSTEM_ERROR);
        if (productOrder.getStatus() != OrderState.ORDER_WAIT_PAY) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "订单状态错误");
        }
        productOrder.setStatus(OrderState.ORDER_FINISH_PAY);
        // 修改缓存
        // 命令模式进行相关处理
        return true;
    }
}
