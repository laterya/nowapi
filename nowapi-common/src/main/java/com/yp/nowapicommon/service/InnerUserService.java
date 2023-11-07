package com.yp.nowapicommon.service;

/**
 * @author yp
 * @date: 2023/11/7
 */
public interface InnerUserService {

    /**
     * 验证请求头中的各参数
     * @param accessKey 密钥
     * @param nonce 随机数
     * @param body 请求体（参数）
     * @param timestamp 时间戳
     * @param sign 签名
     * @return 返回是否验证成功
     */
    long verifyAndGetUserId(String accessKey, String nonce, String body, String timestamp, String sign);

    /**
     * 判断所调用接口是否存在且是否上线
     * @param path 请求路径
     * @param method 请求方法
     * @return 是否可用
     */
    long verifyAndGetInterfaceInfoId(String path, String method);
}
