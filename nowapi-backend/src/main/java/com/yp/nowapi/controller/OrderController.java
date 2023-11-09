package com.yp.nowapi.controller;

import com.yp.nowapi.common.BaseResponse;
import com.yp.nowapi.common.ErrorCode;
import com.yp.nowapi.common.ResultUtils;
import com.yp.nowapi.exception.BusinessException;
import com.yp.nowapi.model.dto.order.PayCreateRequest;
import com.yp.nowapi.model.entity.ProductOrder;
import com.yp.nowapi.model.entity.User;
import com.yp.nowapi.service.ProductOrderService;
import com.yp.nowapi.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author yp
 * @date: 2023/11/9
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private UserService userService;

    @Resource
    private ProductOrderService productOrderService;

    @PostMapping("/createOrder")
    public BaseResponse<ProductOrder> createOrder(@RequestBody PayCreateRequest payCreateRequest, HttpServletRequest request) {
        if (ObjectUtils.anyNull(payCreateRequest) || payCreateRequest.getProductId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long productId = payCreateRequest.getProductId();
        User loginUser = userService.getLoginUser(request);
        ProductOrder productOrder = productOrderService.createOrder(productId, loginUser);
        if (productOrder == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "订单创建失败，请稍后再试");
        }
        return ResultUtils.success(productOrder);
    }
}
