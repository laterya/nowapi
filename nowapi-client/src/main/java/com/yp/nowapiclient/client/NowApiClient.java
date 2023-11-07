package com.yp.nowapiclient.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yp.nowapicommon.entity.User;

import java.util.HashMap;

import static com.yp.nowapiclient.utils.ReqHeaderGenUtils.getHeaderMap;

/**
 * @author yp
 * @date: 2023/11/6
 */
public class NowApiClient {
    private static final String GATEWAY_HOST = "http://localhost:8103";

    private String accessKey;

    private String secretKey;

    public NowApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByGet(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.get(GATEWAY_HOST + "/api/demo/", paramMap);
        System.out.println(result);
        return result;
    }

    public String getNameByPost(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result = HttpUtil.post(GATEWAY_HOST + "/api/demo/", paramMap);
        System.out.println(result);
        return result;
    }


    public String getUsernameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/demo/user")
                .addHeaders(getHeaderMap(json, accessKey, secretKey))
                .body(json)
                .execute();
        System.out.println(httpResponse.getStatus());
        String result = httpResponse.body();
        System.out.println(result);
        return result;
    }
}
