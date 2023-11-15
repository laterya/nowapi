package com.yp.nowapi.adapter;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yp.nowapi.model.vo.LoginUserVO;
import com.yp.nowapi.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author yp
 * @date: 2023/11/15
 */
@Component
public class Login3rdAdapter implements Login3rdTarget {

    @Resource
    private UserService userService;

    @Value("${gitee.tokenUrl}")
    private String accessTokenUri;

    @Value("${gitee.userInfoUrl}")
    private String userInfoUri;

    @Value("${gitee.userPrefix}")
    private String userPrefix;

    @Value("${gitee.state}")
    private String state;

    @Override
    public LoginUserVO loginByGitee(String code, String state) {
        // 进行state判断，state的值是前端与后端商定好的，前端将state传递给gitee平台，gitee平台会将state原样返回给回调接口
        if (!state.equals(this.state)) {
            throw new UnsupportedOperationException("state不正确");
        }
        // 通过code获取accessToken,code从回调接口中获取
        String token = null;
        try (HttpResponse response = HttpUtil.createPost(accessTokenUri + code).execute()) {
            String body = response.body();
            token = JSONUtil.parseObj(body).getStr("access_token");
        }
        // 通过accessToken获取用户信息
        String userName = null;
        String userAccount = null;
        try (HttpResponse execute = HttpUtil.createGet(userInfoUri + token).execute()) {
            String body = execute.body();
            JSONObject entries = JSONUtil.parseObj(body);
            userAccount = entries.getStr("id");
        }

        return autoRegister3rdAndLogin(userAccount);
    }

    private LoginUserVO autoRegister3rdAndLogin(String userAccount) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        if (!userService.checkUserExist(userAccount)) {
            userService.userRegister(userAccount, "12345678", "12345678");
        }
        return userService.userLogin(userAccount, "12345678", request);
    }
}
