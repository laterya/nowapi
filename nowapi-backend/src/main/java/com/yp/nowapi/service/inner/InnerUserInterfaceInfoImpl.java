package com.yp.nowapi.service.inner;

import com.yp.nowapi.service.UserInterfaceInfoService;
import com.yp.nowapicommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author yp
 * @date: 2023/11/7
 */
@DubboService
public class InnerUserInterfaceInfoImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public void invokeCount(long interfaceInfoId, long userId) {
        userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }
}
