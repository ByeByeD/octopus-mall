package com.octopusmall.common.global.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.octopusmall.common.global.entity.OtpsUser;
import com.octopusmall.common.global.entity.OtpsUserHis;
import org.apache.ibatis.annotations.Param;

public interface OtpsUserMapper extends BaseMapper<OtpsUser> {

    /**
     * 自定义新增用户
     */
    int insertUser(@Param("user") OtpsUser user);

    /**
     * 根据账号查询未删除用户
     */
    OtpsUser selectByAccount(@Param("account") String account);

    /**
     * 根据手机号查询未删除用户，用于校验手机号唯一
     */
    OtpsUser selectByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    /**
     * 插入用户历史记录
     */
    int insertUserHis(@Param("userHis") OtpsUserHis userHis);

    /**
     * 根据ID查询未删除用户
     */
    OtpsUser selectById(@Param("id") String id);
}