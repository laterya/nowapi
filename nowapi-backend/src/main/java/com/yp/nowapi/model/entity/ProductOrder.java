package com.yp.nowapi.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yp.nowapi.state.OrderState;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品订单
 *
 * @TableName product_order
 */
@TableName(value = "product_order")
@Data
public class ProductOrder implements Serializable {
    /**
     * 订单号
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String orderName;

    /**
     * 金额(分)
     */
    private Long total;

    /**
     * 二维码地址
     */
    private String codeUrl;

    /**
     * 订单状态
     */
    private OrderState status;

    /**
     * 支付宝formData
     */
    private String formData;

    /**
     * 过期时间
     */
    private Date expirationTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}