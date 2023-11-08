package com.yp.nowapi.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 */
@Data
public class InterfaceInfoUpdateRequest implements Serializable {

    private static final long serialVersionUID = 6330251227810599049L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口地址
     */
    private String url;
    /**
     * 请求方法
     */
    private String method;

    /**
     * 接口请求参数
     */
    private String requestParams;

    /**
     * 接口响应参数
     */
    private String responseParams;

    /**
     * 扣除积分数
     */
    private Long reduceScore;

    /**
     * 请求示例
     */
    private String requestExample;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 返回格式(JSON等等)
     */
    private String returnFormat;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 接口头像
     */
    private String avatarUrl;
}