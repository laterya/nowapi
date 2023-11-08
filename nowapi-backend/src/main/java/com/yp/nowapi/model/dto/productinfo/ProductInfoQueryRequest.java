package com.yp.nowapi.model.dto.productinfo;

import com.yp.nowapi.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author yp
 * @date: 2023/11/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductInfoQueryRequest extends PageRequest implements Serializable {
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

    private static final long serialVersionUID = 1L;
}
