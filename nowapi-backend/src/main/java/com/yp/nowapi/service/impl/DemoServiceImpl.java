package com.yp.nowapi.service.impl;

import com.yp.nowapicommon.service.DemoService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author yp
 * @date: 2023/11/7
 */
@DubboService
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
