package com.yp.nowapitest;


import com.yp.nowapiclient.client.NowApiClient;
import com.yp.nowapicommon.entity.User;

/**
 * @author yp
 * @date: 2023/11/6
 */
public class Main {
    public static void main(String[] args) {
        NowApiClient nowApiClient = new NowApiClient("12", "23");
        System.out.println(nowApiClient.getNameByGet("yp"));
        System.out.println(nowApiClient.getNameByPost("djwao"));
        System.out.println(nowApiClient.getUsernameByPost(new User("hfvsdoiufv")));

    }
}
