package com.yp.nowapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yp.nowapi.model.entity.ProductOrder;
import com.yp.nowapi.model.entity.User;

public interface ProductOrderService extends IService<ProductOrder> {

    ProductOrder createOrder(Long productId, User loginUser);
}
