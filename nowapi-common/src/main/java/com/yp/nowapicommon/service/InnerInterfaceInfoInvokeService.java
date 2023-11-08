package com.yp.nowapicommon.service;

/**
 * @author yp
 * @date: 2023/11/7
 */
public interface InnerInterfaceInfoInvokeService {
    /**
     * 调用接口并计数
     * @param interfaceInfoId
     * @param userId
     */
    void invokeCount(long interfaceInfoId, long userId);

    boolean isEnoughInvokeCount(long interfaceInfoId, long userId);
}
