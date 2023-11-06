package com.yp.nowapi.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 上下线请求
 *
 */
@Data
public class IdRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}