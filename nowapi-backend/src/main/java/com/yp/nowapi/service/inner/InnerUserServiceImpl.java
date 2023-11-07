package com.yp.nowapi.service.inner;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yp.nowapi.mapper.InterfaceInfoMapper;
import com.yp.nowapi.mapper.UserKeyMapper;
import com.yp.nowapi.model.entity.InterfaceInfo;
import com.yp.nowapi.model.entity.UserKey;
import com.yp.nowapicommon.service.InnerUserService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author yp
 * @date: 2023/11/7
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserKeyMapper userKeyMapper;

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    /**
     * 验证请求头中的各参数
     *
     * @param accessKey 密钥
     * @param nonce     随机数
     * @param body      请求体（参数）
     * @param timestamp 时间戳
     * @param sign      签名
     * @return 返回是否验证成功
     */
    @Override
    public long verifyAndGetUserId(String accessKey, String nonce, String body, String timestamp, String sign) {
        LambdaQueryWrapper<UserKey> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserKey::getAccessKey, accessKey);
        UserKey userKey = userKeyMapper.selectOne(lqw);
        if (userKey == null) {
            return -1;
        }
        // todo 验证随机数是否重复
        if (nonce.length() > 4) {
            return -1;
        }
        // 验证时间戳是否过期
        if (System.currentTimeMillis() / 1000 - Long.parseLong(timestamp) > 60) {
            return -1;
        }
        // 验证签名是否正确
        String secretKey = userKey.getSecretKey();
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        String content = body + "." + secretKey;
        String genSign = digester.digestHex(content);

        if (genSign.equals(sign)) {
            return userKey.getUserId();
        }
        return -1;
    }

    /**
     * 判断所调用接口是否存在且是否上线
     *
     * @param path   请求路径
     * @param method 请求方法
     * @return 是否可用
     */
    @Override
    public long verifyAndGetInterfaceInfoId(String path, String method) {
        LambdaQueryWrapper<InterfaceInfo> lqw = new LambdaQueryWrapper<>();
        lqw.eq(InterfaceInfo::getUrl, path);
        lqw.eq(InterfaceInfo::getMethod, method);
        InterfaceInfo interfaceInfo = interfaceInfoMapper.selectOne(lqw);
        if (interfaceInfo != null && interfaceInfo.getStatus() == 1) {
            return interfaceInfo.getId();
        }
        return -1;
    }
}
