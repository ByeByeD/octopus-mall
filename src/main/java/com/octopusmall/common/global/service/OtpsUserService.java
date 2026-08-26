package com.octopusmall.common.global.service;

import com.octopusmall.common.global.dto.LoginReqDto;
import com.octopusmall.common.global.dto.LoginRespDto;
import com.octopusmall.common.global.dto.RegisterReqDto;
import com.octopusmall.common.global.dto.RegisterRespDto;
import com.octopusmall.common.global.dto.UpdatePwdReqDto;
import com.octopusmall.common.global.dto.UpdateUserInfoReqDto;
import com.octopusmall.common.global.entity.OtpsUser;
import jakarta.servlet.http.HttpServletRequest;

public interface OtpsUserService {
    RegisterRespDto register(RegisterReqDto req);

    LoginRespDto login(LoginReqDto reqDto);

    boolean updatePwd(UpdatePwdReqDto reqDto);

    boolean logout();

    /**
     * 修改用户信息（不包括ID、PASSWORD、CREATE_TIME等字段）
     * 修改前会先向历史表插入修改前的数据
     */
    boolean updateUserInfo(UpdateUserInfoReqDto reqDto);
}