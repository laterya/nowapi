package com.yp.nowapi.adapter;

import com.yp.nowapi.model.vo.LoginUserVO;
import com.yp.nowapi.model.vo.UserVO;

/**
 * @author yp
 * @date: 2023/11/15
 */
public interface Login3rdTarget {

    public LoginUserVO loginByGitee(String code, String state);
}
