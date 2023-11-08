package com.yp.nowapi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yp.nowapi.annotation.AuthCheck;
import com.yp.nowapi.common.BaseResponse;
import com.yp.nowapi.common.DeleteRequest;
import com.yp.nowapi.common.ErrorCode;
import com.yp.nowapi.common.ResultUtils;
import com.yp.nowapi.constant.CommonConstant;
import com.yp.nowapi.constant.UserConstant;
import com.yp.nowapi.exception.BusinessException;
import com.yp.nowapi.model.dto.productinfo.ProductInfoAddRequest;
import com.yp.nowapi.model.dto.productinfo.ProductInfoQueryRequest;
import com.yp.nowapi.model.dto.productinfo.ProductInfoUpdateRequest;
import com.yp.nowapi.model.entity.ProductInfo;
import com.yp.nowapi.model.entity.User;
import com.yp.nowapi.service.ProductInfoService;
import com.yp.nowapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/productInfo")
@Slf4j
public class ProductInfoController {

    @Resource
    private ProductInfoService productInfoService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 新增积分充值产品
     *
     * @param productInfoAddRequest 新增请求
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addProductInfo(@RequestBody ProductInfoAddRequest productInfoAddRequest, HttpServletRequest request) {
        if (productInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ProductInfo productInfo = new ProductInfo();
        BeanUtils.copyProperties(productInfoAddRequest, productInfo);
        // 校验
        productInfoService.validProductInfo(productInfo, true);
        User loginUser = userService.getLoginUser(request);
        productInfo.setUserId(loginUser.getId());
        boolean result = productInfoService.save(productInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(productInfo.getId());
    }

    /**
     * 删除商品
     *
     * @param deleteRequest 删除请求
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteProductInfo(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        // 判断是否存在
        ProductInfo oldProductInfo = productInfoService.getById(id);
        if (oldProductInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(productInfoService.removeById(id));
    }

    /**
     * 更新商品信息
     *
     * @param productInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateProductInfo(@RequestBody ProductInfoUpdateRequest productInfoUpdateRequest) {
        if (productInfoUpdateRequest == null || productInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ProductInfo productInfo = new ProductInfo();
        BeanUtils.copyProperties(productInfoUpdateRequest, productInfo);
        // 参数校验
        productInfoService.validProductInfo(productInfo, false);
        long id = productInfoUpdateRequest.getId();
        // 判断是否存在
        ProductInfo oldProductInfo = productInfoService.getById(id);
        if (oldProductInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(productInfoService.updateById(productInfo));
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<ProductInfo> getProductInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ProductInfo productInfo = productInfoService.getById(id);
        return ResultUtils.success(productInfo);
    }

    /**
     * 获取列表（仅管理员可使用）
     *
     * @param productInfoQueryRequest
     * @return
     */
    @AuthCheck(mustRole = "admin")
    @PostMapping("/list")
    public BaseResponse<List<ProductInfo>> listProductInfo(@RequestBody ProductInfoQueryRequest productInfoQueryRequest) {
        ProductInfo productInfoQuery = new ProductInfo();
        if (productInfoQueryRequest != null) {
            BeanUtils.copyProperties(productInfoQueryRequest, productInfoQuery);
        }
        QueryWrapper<ProductInfo> queryWrapper = new QueryWrapper<>(productInfoQuery);
        List<ProductInfo> productInfoList = productInfoService.list(queryWrapper);
        return ResultUtils.success(productInfoList);
    }

    /**
     * 分页获取列表
     *
     * @param productInfoQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<ProductInfo>> listProductInfoByPage(@RequestBody ProductInfoQueryRequest productInfoQueryRequest, HttpServletRequest request) {
        if (productInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ProductInfo productInfoQuery = new ProductInfo();
        BeanUtils.copyProperties(productInfoQueryRequest, productInfoQuery);
        long current = productInfoQueryRequest.getCurrent();
        long size = productInfoQueryRequest.getPageSize();
        String sortField = productInfoQueryRequest.getSortField();
        String sortOrder = productInfoQueryRequest.getSortOrder();
        String description = productInfoQuery.getDescription();
        // description 需支持模糊搜索
        productInfoQuery.setDescription(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<ProductInfo> queryWrapper = new QueryWrapper<>(productInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<ProductInfo> productInfoPage = productInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(productInfoPage);
    }

    // endregion
}
