package com.yp.nowapi.model.dto.productinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yp
 * @date: 2023/11/8
 */
@Data
public class ProductInfoUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1159903537300940115L;

    private Long id;
    /**
     * 产品名称
     */
    private String name;

    /**
     * 产品描述
     */
    private String description;

    /**
     * 金额(分)
     */
    private Long price;

    /**
     * 增加积分个数
     */
    private Long addPoints;

    /**
     * 商品状态（0- 默认下线 1- 上线）
     */
    private Integer status;

}
