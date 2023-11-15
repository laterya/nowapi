package com.yp.nowapi.bridge.function;

import com.yp.nowapi.model.vo.LoginUserVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yp
 * @date: 2023/11/15
 */
public interface RegisterLoginFuncInterface {
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    public long userRegister(String userAccount, String userPassword, String checkPassword);

    public boolean checkUserExist(String userAccount);

    public LoginUserVO login3rd(HttpServletRequest request);
}
