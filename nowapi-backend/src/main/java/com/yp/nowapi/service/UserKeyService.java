package com.yp.nowapi.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yp.nowapi.model.entity.UserKey;

public interface UserKeyService extends IService<UserKey> {

    void addPoints(UserKey userKey, Long id);
}
