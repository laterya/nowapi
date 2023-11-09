package com.yp.nowapi.payment;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yp
 * @date: 2023/11/9
 */
@Data
@ConfigurationProperties(prefix = "nowapi.alipay")
@Component
public class AliPayBean {
    /**
     * appId
     */
    private String appId;
    /**
     * 私钥
     */
    private String privateKey;
    /**
     * 支付宝公钥
     */
    private String publicKey;

    /**
     * 支付宝支付网关
     */
    private String serverUrl;

    /**
     * 外网访问项目域名
     */
    private String domain;
}
