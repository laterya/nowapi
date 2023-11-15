package com.yp.nowapi.bridge.function;

import com.yp.nowapi.bridge.abst.factory.RegisterLoginComponentFactory;
import com.yp.nowapi.mapper.UserMapper;
import com.yp.nowapi.model.vo.LoginUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author yp
 * @date: 2023/11/15
 */
@Component
@Slf4j
public class RegisterLoginByDefault extends AbstractRegisterLoginFunc implements RegisterLoginFuncInterface {
    @Resource
    private UserMapper userMapper;

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        return super.commonUserLogin(userAccount, userPassword, request, userMapper);
    }

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        return super.commonUserRegister(userAccount, userPassword, checkPassword, userMapper);
    }

    @Override
    public boolean checkUserExist(String userAccount) {
        return super.commonCheckUserExist(userAccount, userMapper);
    }

    @PostConstruct
    public void initFuncMap() {
        RegisterLoginComponentFactory.funcMap.put("Default", this);
    }

}
