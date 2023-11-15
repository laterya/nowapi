package com.yp.nowapi.bridge.function;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yp.nowapi.bridge.abst.factory.RegisterLoginComponentFactory;
import com.yp.nowapi.mapper.UserMapper;
import com.yp.nowapi.model.vo.LoginUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class RegisterLoginByGitee extends AbstractRegisterLoginFunc implements RegisterLoginFuncInterface {

    @Resource
    private UserMapper userMapper;

    @Value("${gitee.tokenUrl}")
    private String tokenUrl;

    @Value("${gitee.userInfoUrl}")
    private String userInfoUrl;

    @Value("${gitee.userPrefix}")
    private String userPrefix;

    @Value("${gitee.state}")
    private String state;

    @Override
    public LoginUserVO login3rd(HttpServletRequest request) {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        // 进行state判断，state的值是前端与后端商定好的，前端将state传递给gitee平台，gitee平台会将state原样返回给回调接口
        if (!state.equals(this.state)) {
            throw new UnsupportedOperationException("state不正确");
        }
        // 通过code获取accessToken,code从回调接口中获取
        String token = null;
        try (HttpResponse response = HttpUtil.createPost(tokenUrl + code).execute()) {
            String body = response.body();
            token = JSONUtil.parseObj(body).getStr("access_token");
        }
        // 通过accessToken获取用户信息
        String userName = null;
        String userAccount = null;
        try (HttpResponse execute = HttpUtil.createGet(userInfoUrl + token).execute()) {
            String body = execute.body();
            JSONObject entries = JSONUtil.parseObj(body);
            userAccount = entries.getStr("id");
        }

        return autoRegister3rdAndLogin(userAccount, request);
    }

    private LoginUserVO autoRegister3rdAndLogin(String userAccount, HttpServletRequest request) {
        if (!this.commonCheckUserExist(userAccount, userMapper)) {
            super.commonUserRegister(userAccount, "12345678", "12345678", userMapper);
        }
        return this.commonUserLogin(userAccount, "12345678", request, userMapper);
    }

    @PostConstruct
    private void initFuncMap() {
        RegisterLoginComponentFactory.funcMap.put("GITEE", this);
    }
}
