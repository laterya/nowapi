package com.yp.nowapi.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yp.nowapi.mapper.UserKeyMapper;
import com.yp.nowapi.model.entity.UserKey;
import com.yp.nowapi.service.UserKeyService;
import org.springframework.stereotype.Service;

@Service
public class UserKeyServiceImpl extends ServiceImpl<UserKeyMapper, UserKey>
    implements UserKeyService {

}




