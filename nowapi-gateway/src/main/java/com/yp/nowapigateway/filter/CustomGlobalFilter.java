package com.yp.nowapigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * @author yp
 * @date: 2023/11/7
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    private static final List<String> IP_WHITE_LIST = Collections.singletonList("127.0.0.1");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        StopWatch stopWatch = new StopWatch("global filter");
        stopWatch.start();
        // 1. 请求日志
        ServerHttpRequest request = exchange.getRequest();
        String url = request.getPath().value();
        String reqParam = request.getQueryParams().toString();
        String address = request.getLocalAddress().getHostString();
        log.info("local address: {}, remote address: {}", address, request.getRemoteAddress());
        log.info("request start，id: {}, path: {}, params: {}", request.getId(), url, reqParam);
        // 2. 黑白名单
        ServerHttpResponse response = exchange.getResponse();
        if (!IP_WHITE_LIST.contains(address)) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }
        // 3. 用户鉴权 todo 从数据库获取accessKey和secretKey并判断请求头中签名是否正确，校验随机数与时间戳防止盗刷
        // 4. 请求的模拟接口是否存在 todo 从数据库查询模拟接口是否存在，以及请求方法是否匹配（校验请求参数等）
        // 5. 请求转发，调用模拟接口
        Mono<Void> filter = chain.filter(exchange);
        stopWatch.stop();
        // 6. 响应日志 todo 使用装饰者模式修改响应时间
        log.info("request end, id: {}, response status: {}, s: {}", request.getId(), response.getStatusCode(), stopWatch.getTotalTimeMillis() / 1000);
        if (response.getStatusCode() == HttpStatus.OK) {
            // 7. 调用成功，接口调用次数加一 todo 对接口次数计数，可以使用mq异步处理
        } else {
            // 8. 调用失败，返回错误信息
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return response.setComplete();
        }
        log.info("CustomGlobalFilter");
        return filter;
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
