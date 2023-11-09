package com.yp.nowapi.payment;

import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class AliPayConfig {

    @Resource
    private AliPayBean aliPayBean;

    @Bean
    public void aliPayApi() {
        AliPayApiConfig aliPayApiConfig = AliPayApiConfig.builder()
                .setAppId(aliPayBean.getAppId())
                .setAliPayPublicKey(aliPayBean.getPublicKey())
                .setCharset("UTF-8")
                .setPrivateKey(aliPayBean.getPrivateKey())
                .setServiceUrl(aliPayBean.getServerUrl())
                .setSignType("RSA2")
                .setCertModel(false)
                .build(); // 普通公钥方式
        AliPayApiConfigKit.setThreadLocalAliPayApiConfig(aliPayApiConfig);
    }
}
