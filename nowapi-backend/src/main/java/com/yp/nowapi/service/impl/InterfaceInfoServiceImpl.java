package com.yp.nowapi.service.impl;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yp.nowapi.common.ErrorCode;
import com.yp.nowapi.exception.BusinessException;
import com.yp.nowapi.mapper.InterfaceInfoMapper;
import com.yp.nowapi.model.entity.InterfaceInfo;
import com.yp.nowapi.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.yp.nowapiclient.utils.ReqHeaderGenUtils.getHeaderMap;


@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Value("${nowapi.client.access-key}")
    private String accessKey;

    @Value("${nowapi.client.secret-key}")
    private String secretKey;

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }

    @Override
    public void invokeTest(InterfaceInfo interfaceInfo) {
        String url = interfaceInfo.getUrl();
        String method = interfaceInfo.getMethod();
        String requestParams = interfaceInfo.getRequestParams();

        HttpResponse httpResponse = invokeRemote(requestParams, url, method);
        httpResponse.close();
    }

    @Override
    public Object invoke(InterfaceInfo interfaceInfo, String userRequestParams) {
        String url = interfaceInfo.getUrl();
        String method = interfaceInfo.getMethod();

        HttpResponse httpResponse = invokeRemote(userRequestParams, url, method);
        String result = httpResponse.body();
        httpResponse.close();
        return result;
    }

    private HttpResponse invokeRemote(String userRequestParams, String url, String method) {
        HttpResponse httpResponse = null;
        if (method.equals("GET")) {
            httpResponse = HttpRequest.get(url)
                    .addHeaders(getHeaderMap(userRequestParams, accessKey, secretKey))
                    .body(userRequestParams)
                    .execute();
        } else {
            httpResponse = HttpRequest.post(url)
                    .addHeaders(getHeaderMap(userRequestParams, accessKey, secretKey))
                    .body(userRequestParams)
                    .execute();
        }

        if (!httpResponse.isOk()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口调用失败");
        }
        return httpResponse;
    }
}




