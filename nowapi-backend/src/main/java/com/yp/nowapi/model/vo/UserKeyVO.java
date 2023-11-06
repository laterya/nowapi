package com.yp.nowapi.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class UserKeyVO implements Serializable {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    private static final long serialVersionUID = 1L;
}