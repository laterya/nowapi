package com.yp.nowapi.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yp.nowapi.common.ErrorCode;
import com.yp.nowapi.exception.BusinessException;
import com.yp.nowapi.mapper.UserKeyMapper;
import com.yp.nowapi.model.entity.ProductInfo;
import com.yp.nowapi.model.entity.UserKey;
import com.yp.nowapi.service.ProductInfoService;
import com.yp.nowapi.service.UserKeyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserKeyServiceImpl extends ServiceImpl<UserKeyMapper, UserKey>
        implements UserKeyService {

    @Resource
    private ProductInfoService productInfoService;

    @Override
    public void addPoints(UserKey userKey, Long id) {
        ProductInfo productInfo = productInfoService.getById(id);
        if (productInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "商品不存在");
        }
        userKey.setLeftNum(userKey.getLeftNum() + productInfo.getAddPoints());
        baseMapper.updateById(userKey);
    }
}




