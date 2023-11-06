package com.yp.nowapiclient.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * @author yp
 * @date: 2023/11/6
 */
public class SignUtils {

    public static String genSign(String body, String secretKey) {
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        String content = body + "." + secretKey;
        return digester.digestHex(content);
    }
}
