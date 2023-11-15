package com.yp.nowapi.bridge.abst.factory;

import com.yp.nowapi.bridge.abst.AbstractRegisterLoginComponent;
import com.yp.nowapi.bridge.abst.RegisterLoginComponent;
import com.yp.nowapi.bridge.function.RegisterLoginFuncInterface;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yp
 * @date: 2023/11/15
 */
public class RegisterLoginComponentFactory {
    // 根据不同的登陆方式，获取不同的登陆组件
    public static Map<String, AbstractRegisterLoginComponent> componentMap = new ConcurrentHashMap<>();

    // 缓存不同类型的实现类
    public static Map<String, RegisterLoginFuncInterface> funcMap = new ConcurrentHashMap<>();

    public static AbstractRegisterLoginComponent getComponent(String type) {
        AbstractRegisterLoginComponent component = componentMap.get(type);
        if (component == null) {
            synchronized (componentMap) {
                component = componentMap.get(type);
                if (component == null) {
                    component = new RegisterLoginComponent(funcMap.get(type));
                    componentMap.put(type, component);
                }
            }
        }
        return component;
    }
}
