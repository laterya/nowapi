package com.yp.nowapi.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yp.nowapi.model.entity.InterfaceInfo;

public interface InterfaceInfoService extends IService<InterfaceInfo> {
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

}
