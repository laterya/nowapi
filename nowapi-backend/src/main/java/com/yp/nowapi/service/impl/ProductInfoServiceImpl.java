package com.yp.nowapi.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yp.nowapi.common.ErrorCode;
import com.yp.nowapi.exception.BusinessException;
import com.yp.nowapi.mapper.ProductInfoMapper;
import com.yp.nowapi.model.entity.ProductInfo;
import com.yp.nowapi.service.ProductInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements ProductInfoService {

    @Override
    public void validProductInfo(ProductInfo productInfo, boolean add) {
        Long addPoints = productInfo.getAddPoints();
        String name = productInfo.getName();
        String description = productInfo.getDescription();
        Long price = productInfo.getPrice();

        if (add) {
            if (StringUtils.isAnyBlank(name, description) || addPoints == null || price == null || addPoints <= 0 || price <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
    }
}




