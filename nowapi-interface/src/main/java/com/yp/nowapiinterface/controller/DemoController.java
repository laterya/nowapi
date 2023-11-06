package com.yp.nowapiinterface.controller;


import com.yp.nowapicommon.entity.User;
import org.springframework.web.bind.annotation.*;

/**
 * @author yp
 * @date: 2023/11/3
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @GetMapping("/")
    public String getNameByGet(String name) {
        return "Get 你的名字是：" + name;
    }

    @PostMapping("/")
    public String getNameByPost(@RequestParam String name) {
        return "Post 你的名字是：" + name;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user) {
        return "Post 用户的名字是：" + user.getUserName();
    }
}
