package com.octopusmall.common.global.controller;

import com.octopusmall.common.annotation.IgnoreLoginValid;
import com.octopusmall.common.annotation.Permission;
import com.octopusmall.common.global.dto.LoginReqDto;
import com.octopusmall.common.global.dto.LoginRespDto;
import com.octopusmall.common.global.dto.RegisterReqDto;
import com.octopusmall.common.global.dto.ResponseDto;
import com.octopusmall.common.global.dto.UpdatePwdReqDto;
import com.octopusmall.common.global.dto.AddUserPermissionReqDto;
import com.octopusmall.common.global.dto.DeleteUserPermissionReqDto;
import com.octopusmall.common.global.dto.UpdateUserInfoReqDto;
import com.octopusmall.common.global.dto.UserInfoRespDto;
import com.octopusmall.common.global.service.OtpsUserPermissionService;
import com.octopusmall.common.global.service.OtpsUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common/auth")
@RequiredArgsConstructor
public class AuthCommonController {

    private final OtpsUserService otpsUserService;
    private final OtpsUserPermissionService otpsUserPermissionService;

    /**
     * 用户注册接口
     * @param req 注册请求参数
     * @return 注册结果
     */
    @IgnoreLoginValid // 不需要进行用户登录校验
    @PostMapping("/register")
    public ResponseDto register(@Valid @RequestBody RegisterReqDto req) {
        UserInfoRespDto register = otpsUserService.register(req);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResultCode("0");
        responseDto.setResultMsg("success");
        responseDto.setResultData(register);
        return responseDto;
    }

    /**
     * 登录，公开接口
     */
    @IgnoreLoginValid // 不需要进行用户登录校验
    @PostMapping("/login")
    public ResponseDto login(@Valid @RequestBody LoginReqDto reqDto) {
        LoginRespDto user = otpsUserService.login(reqDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResultCode("0");
        responseDto.setResultMsg("success");
        responseDto.setResultData(user);
        return responseDto;
    }

    /**
     * 修改密码
     */
    @PostMapping("/updatePwd")
    @Permission(name = "user:updatePwd")
    public ResponseDto updatePwd(@Valid @RequestBody UpdatePwdReqDto reqDto) {
        otpsUserService.updatePwd(reqDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResultCode("0");
        responseDto.setResultMsg("success");
        return responseDto;
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    @Permission(name = "user:logout")
    public ResponseDto logout() {
        otpsUserService.logout();
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResultCode("0");
        responseDto.setResultMsg("success");
        return responseDto;
    }

    /**
     * 修改用户信息（不包括ID、PASSWORD、CREATE_TIME等）
     * 修改前会先向历史表插入修改前的数据
     */
    @PostMapping("/updateUserInfo")
    @Permission(name = "user:updateUserInfo")
    public ResponseDto updateUserInfo(@Valid @RequestBody UpdateUserInfoReqDto reqDto) {
        UserInfoRespDto userInfo = otpsUserService.updateUserInfo(reqDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResultCode("0");
        responseDto.setResultMsg("success");
        responseDto.setResultData(userInfo);
        return responseDto;
    }

    /**
     * 获取当前登录用户信息
     */
    @PostMapping("/getUserInfo")
    @Permission(name = "user:getInfo")
    public ResponseDto getUserInfo() {
        UserInfoRespDto userInfo = otpsUserService.getUserInfo();
        ResponseDto responseDto = new ResponseDto("0", "success");
        responseDto.setResultData(userInfo);
        return responseDto;
    }

    /**
     * todo 用户权限修改，管理员修改用户权限或用户权限到期时刷新redis缓存！！！
     * 1、管理员修改用户权限实现
     * 提供接口供管理员修改用户权限，分别提供新增用户权限接口和删除用户权限接口，修改完后刷新redis缓存（如果存在用户权限缓存）
     * 2、用户权限到期刷新
     * 通过定时任务扫描用户权限表，权限过期用户进行redis权限刷新，如果存在redis中，直接删除key
     */

    /**
     * 批量新增用户权限（管理员接口）
     * 支持同时新增多个权限
     */
    @PostMapping("/addUserPermissions")
    @Permission(name = "permission:add")
    public ResponseDto addUserPermissions(@Valid @RequestBody AddUserPermissionReqDto reqDto) {
        otpsUserPermissionService.addUserPermissions(reqDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResultCode("0");
        responseDto.setResultMsg("success");
        return responseDto;
    }

    /**
     * 批量删除用户权限（管理员接口）
     * 支持同时删除多个权限
     */
    @PostMapping("/deleteUserPermissions")
    @Permission(name = "permission:delete")
    public ResponseDto deleteUserPermissions(@Valid @RequestBody DeleteUserPermissionReqDto reqDto) {
        otpsUserPermissionService.deleteUserPermissions(reqDto);
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResultCode("0");
        responseDto.setResultMsg("success");
        return responseDto;
    }
}