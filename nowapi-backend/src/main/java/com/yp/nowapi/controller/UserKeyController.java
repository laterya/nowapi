package com.yp.nowapi.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yp.nowapi.common.BaseResponse;
import com.yp.nowapi.common.ErrorCode;
import com.yp.nowapi.common.ResultUtils;
import com.yp.nowapi.exception.BusinessException;
import com.yp.nowapi.model.entity.User;
import com.yp.nowapi.model.entity.UserKey;
import com.yp.nowapi.model.vo.UserKeyVO;
import com.yp.nowapi.service.UserKeyService;
import com.yp.nowapi.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author yp
 * @date: 2023/11/6
 */
@RestController
@RequestMapping("/userKey")
public class UserKeyController {

    @Resource
    private UserService userService;

    @Resource
    private UserKeyService userKeyService;

    @PostMapping
    public BaseResponse<Boolean> addUserKey(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        UserKey userKey = new UserKey();
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        String accessKey = digester.digestHex(RandomUtil.randomString(6) + loginUser.getUserAccount());
        String secretKey = digester.digestHex(RandomUtil.randomString(6) + loginUser.getUserPassword());
        userKey.setUserId(loginUser.getId());
        userKey.setAccessKey(accessKey);
        userKey.setSecretKey(secretKey);
        try {
            userKeyService.save(userKey);
        } catch (RuntimeException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户密钥添加失败");
        }
        return ResultUtils.success(true);
    }

    @GetMapping
    public BaseResponse<UserKeyVO> getUserKey(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        LambdaQueryWrapper<UserKey> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserKey::getUserId, loginUser.getId());
        UserKey userKey = userKeyService.getOne(lqw);
        if (userKey == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户密钥不存在");
        }
        UserKeyVO userKeyVO = new UserKeyVO();
        BeanUtils.copyProperties(userKey, userKeyVO);
        return ResultUtils.success(userKeyVO);
    }

}
