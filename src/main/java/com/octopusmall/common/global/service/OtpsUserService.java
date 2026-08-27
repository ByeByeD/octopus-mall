package com.octopusmall.common.global.service;

import com.octopusmall.common.global.dto.LoginReqDto;
import com.octopusmall.common.global.dto.LoginRespDto;
import com.octopusmall.common.global.dto.RegisterReqDto;
import com.octopusmall.common.global.dto.UpdatePwdReqDto;
import com.octopusmall.common.global.dto.UpdateUserInfoReqDto;
import com.octopusmall.common.global.dto.UserInfoRespDto;

public interface OtpsUserService {
    UserInfoRespDto register(RegisterReqDto req);

    LoginRespDto login(LoginReqDto reqDto);

    boolean updatePwd(UpdatePwdReqDto reqDto);

    boolean logout();

    /**
     * 修改用户信息（不包括ID、PASSWORD、CREATE_TIME等字段）
     * 修改前会先向历史表插入修改前的数据
     */
    UserInfoRespDto updateUserInfo(UpdateUserInfoReqDto reqDto);

    /**
     * 获取当前登录用户信息
     * @return 用户信息
     */
    UserInfoRespDto getUserInfo();
}