package com.yp.nowapi.bridge.abst;

import com.yp.nowapi.bridge.function.RegisterLoginFuncInterface;
import com.yp.nowapi.model.vo.LoginUserVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yp
 * @date: 2023/11/15
 */
public abstract class AbstractRegisterLoginComponent {

    // 面向接口编程，引入registerLoginFuncInterface接口属性，此处即为桥接
    protected RegisterLoginFuncInterface registerLoginFuncInterface;

    public AbstractRegisterLoginComponent(RegisterLoginFuncInterface registerLoginFuncInterface) {
        validate(registerLoginFuncInterface);
        this.registerLoginFuncInterface = registerLoginFuncInterface;
    }

    protected final void validate(RegisterLoginFuncInterface registerLoginFuncInterface) {
        if (!(registerLoginFuncInterface instanceof RegisterLoginFuncInterface)) {
            throw new UnsupportedOperationException("registerLoginFuncInterface must be instance of RegisterLoginFuncInterface");
        }
    }

    public abstract LoginUserVO login(String username, String password);

    public abstract long register(String username, String password);

    protected abstract boolean checkUserExists(String username);

    public abstract LoginUserVO login3rd(HttpServletRequest request);
}
