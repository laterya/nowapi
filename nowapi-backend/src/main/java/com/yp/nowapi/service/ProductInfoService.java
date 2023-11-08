package com.yp.nowapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yp.nowapi.model.entity.ProductInfo;

public interface ProductInfoService extends IService<ProductInfo> {

    void validProductInfo(ProductInfo productInfo, boolean add);
}
