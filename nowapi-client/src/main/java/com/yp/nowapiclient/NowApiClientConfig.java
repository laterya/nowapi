package com.yp.nowapiclient;

import com.yp.nowapiclient.client.NowApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author yp
 * @date: 2023/11/6
 */
@Configuration
@ConfigurationProperties(prefix = "nowapi.client")
@Data
@ComponentScan
public class NowApiClientConfig {

    private String accessKey;

    private String secretKey;

    @Bean
    public NowApiClient nowApiClient() {
        return new NowApiClient(accessKey, secretKey);
    }
}
