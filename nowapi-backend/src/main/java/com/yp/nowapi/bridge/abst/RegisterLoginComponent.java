package com.yp.nowapi.bridge.abst;

import com.yp.nowapi.bridge.function.RegisterLoginFuncInterface;
import com.yp.nowapi.model.vo.LoginUserVO;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yp
 * @date: 2023/11/15
 */
public class RegisterLoginComponent extends AbstractRegisterLoginComponent {
    public RegisterLoginComponent(RegisterLoginFuncInterface registerLoginFuncInterface) {
        super(registerLoginFuncInterface);
    }

    @Override
    public LoginUserVO login(String username, String password) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        return registerLoginFuncInterface.userLogin(username, password, request);
    }

    @Override
    public long register(String username, String password) {
        return registerLoginFuncInterface.userRegister(username, password, password);
    }

    @Override
    protected boolean checkUserExists(String username) {
        return registerLoginFuncInterface.checkUserExist(username);
    }

    @Override
    public LoginUserVO login3rd(HttpServletRequest request) {
        return registerLoginFuncInterface.login3rd(request);
    }
}
