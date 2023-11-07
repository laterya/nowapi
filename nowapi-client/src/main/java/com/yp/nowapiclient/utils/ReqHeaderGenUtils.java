package com.yp.nowapiclient.utils;

import cn.hutool.core.util.RandomUtil;

import java.util.HashMap;
import java.util.Map;

import static com.yp.nowapiclient.utils.SignUtils.genSign;

/**
 * @author yp
 * @date: 2023/11/7
 */
public class ReqHeaderGenUtils {

    public static Map<String, String> getHeaderMap(String body, String accessKey, String secretKey) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("body", body);
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", genSign(body, secretKey));
        return hashMap;
    }
}
