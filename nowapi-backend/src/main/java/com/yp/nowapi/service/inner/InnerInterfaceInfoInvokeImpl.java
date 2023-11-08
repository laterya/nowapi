package com.yp.nowapi.service.inner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yp.nowapi.mapper.InterfaceInfoMapper;
import com.yp.nowapi.mapper.UserKeyMapper;
import com.yp.nowapi.model.entity.InterfaceInfo;
import com.yp.nowapi.model.entity.UserKey;
import com.yp.nowapicommon.service.InnerInterfaceInfoInvokeService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author yp
 * @date: 2023/11/8
 */
@DubboService
public class InnerInterfaceInfoInvokeImpl implements InnerInterfaceInfoInvokeService {
    @Resource
    private UserKeyMapper userKeyMapper;

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;


    @Override
    public void invokeCount(long interfaceInfoId, long userId) {
        InterfaceInfo interfaceInfo = interfaceInfoMapper.selectById(interfaceInfoId);
        Long reduceScore = interfaceInfo.getReduceScore();

        LambdaQueryWrapper<UserKey> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserKey::getUserId, userId);
        UserKey userKey = userKeyMapper.selectOne(lqw);
        Long leftNum = userKey.getLeftNum();

        userKey.setLeftNum(leftNum - reduceScore);
        userKeyMapper.updateById(userKey);
        interfaceInfo.setTotalInvokes(interfaceInfo.getTotalInvokes() + 1);
        interfaceInfoMapper.updateById(interfaceInfo);
    }

    @Override
    public boolean isEnoughInvokeCount(long interfaceInfoId, long userId) {
        InterfaceInfo interfaceInfo = interfaceInfoMapper.selectById(interfaceInfoId);
        LambdaQueryWrapper<UserKey> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserKey::getUserId, userId);
        UserKey userKey = userKeyMapper.selectOne(lqw);

        Long reduceScore = interfaceInfo.getReduceScore();
        Long leftNum = userKey.getLeftNum();
        return leftNum > reduceScore;
    }
}
