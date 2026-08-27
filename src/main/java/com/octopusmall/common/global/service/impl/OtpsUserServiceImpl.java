package com.octopusmall.common.global.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.octopusmall.common.exception.OtpsBaseException;
import com.octopusmall.common.global.dto.LoginReqDto;
import com.octopusmall.common.global.dto.LoginRespDto;
import com.octopusmall.common.global.dto.RegisterReqDto;
import com.octopusmall.common.global.dto.UpdatePwdReqDto;
import com.octopusmall.common.global.dto.UpdateUserInfoReqDto;
import com.octopusmall.common.global.dto.UserInfoRespDto;
import com.octopusmall.common.global.entity.OtpsUser;
import com.octopusmall.common.global.entity.OtpsUserHis;
import com.octopusmall.common.global.mapper.OtpsUserMapper;
import com.octopusmall.common.global.service.OtpsUserService;
import com.octopusmall.common.util.FileUtil;
import com.octopusmall.common.util.CommonUtil;
import com.octopusmall.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor // 自动将final修饰的字段放入构造器中初始化
public class OtpsUserServiceImpl implements OtpsUserService {

    private final OtpsUserMapper otpsUserMapper;

    private final JwtUtil jwtUtil;

    private final StringRedisTemplate stringRedisTemplate;

    private final FileUtil fileUtil;

    // @Value 取值，冒号后面是默认值，yml没配置时生效
    @Value("${auth.redis.jwt-black-prefix}")
    private String jwtBlackPrefix;

    @Value("${auth.redis.user-perm-prefix}")
    private String userPermPrefix;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoRespDto register(RegisterReqDto req) throws RuntimeException {
        //校验账号是否存在
        OtpsUser existAccount = otpsUserMapper.selectByAccount(req.getAccount());
        if (existAccount != null) {
            throw new OtpsBaseException("该账号已注册");
        }
        //手机号非空才校验唯一性
        if(!CommonUtil.isEmpty(req.getPhoneNumber())){
            OtpsUser existPhone = otpsUserMapper.selectByPhoneNumber(req.getPhoneNumber());
            if(existPhone != null){
                throw new OtpsBaseException("该手机号已注册");
            }
        }

        OtpsUser user = new OtpsUser();
        //雪花ID，生成固定20位字符串
        String idStr = CommonUtil.getSnowID();
        user.setId(idStr);
        user.setAccount(req.getAccount());
        user.setPassword(BCrypt.hashpw(req.getPassword()));
        user.setNickname(req.getNickname());
        user.setPhoneNumber(req.getPhoneNumber());
        user.setEmail(req.getEmail());
        user.setType(req.getType());

        //数据库默认值：status='normal'，audit_status='pass'
        user.setStatus("normal");
        user.setAuditStatus("pass");
        user.setAuditRemark(null);
        user.setUserImage(null);

        LocalDateTime now = LocalDateTime.now();
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setIsDeleted(0);

        int rows = otpsUserMapper.insertUser(user);
        if (rows != 1) {
            throw new OtpsBaseException("注册写入数据库失败");
        }

        UserInfoRespDto resp = new UserInfoRespDto();
        resp.setId(user.getId());
        resp.setAccount(user.getAccount());
        resp.setType(user.getType());
        resp.setNickname(user.getNickname());
        return resp;
    }

    @Override
    public LoginRespDto login(LoginReqDto reqDto) {
        OtpsUser user = otpsUserMapper.selectByAccount(reqDto.getAccount());

        if (user == null) {
            throw new OtpsBaseException("账号不存在");
        }
        if ("1".equals(user.getStatus())) {
            throw new OtpsBaseException("账号已被禁用");
        }
        if (!BCrypt.checkpw(reqDto.getPassword(), user.getPassword())) {
            throw new OtpsBaseException("密码错误");
        }
        String token = jwtUtil.generateToken(user.getId());

        LoginRespDto resp = new LoginRespDto();
        resp.setToken(token);
        resp.setId(user.getId());
        resp.setNickname(user.getNickname());
        resp.setType(user.getType());
        resp.setPhoneNumber(user.getPhoneNumber());
        resp.setEmail(user.getEmail());
        resp.setUserImage(user.getUserImage());
        return resp;
    }

    @Override
    public boolean updatePwd(UpdatePwdReqDto reqDto) {
        String userId = CommonUtil.getUserId();
        OtpsUser user = otpsUserMapper.selectOne(new QueryWrapper<OtpsUser>().eq("id", userId));

        if (!BCrypt.checkpw(reqDto.getOldPassword(), user.getPassword())) {
            throw new OtpsBaseException("旧密码校验失败");
        }
        String newHash = BCrypt.hashpw(reqDto.getNewPassword());
        user.setPassword(newHash);
        otpsUserMapper.updateById(user);
        //修改密码清除权限缓存
        stringRedisTemplate.delete(userPermPrefix + userId);
        return true;
    }

    @Override
    public boolean logout() {
        String token = CommonUtil.getToken();
        Claims claims = jwtUtil.parseToken(token);
        long remainMs = jwtUtil.getRemainExpireMs(claims);
        String userId = CommonUtil.getUserId();

        stringRedisTemplate.opsForValue().set(jwtBlackPrefix + token,"1", remainMs, TimeUnit.MILLISECONDS);
        stringRedisTemplate.delete(userPermPrefix + userId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoRespDto updateUserInfo(UpdateUserInfoReqDto reqDto) {
        String userId = CommonUtil.getUserId();

        // 查询当前用户信息
        OtpsUser currentUser = otpsUserMapper.selectById(userId);
        if (currentUser == null) {
            throw new OtpsBaseException("用户不存在");
        }

        // 构造历史记录
        OtpsUserHis userHis = new OtpsUserHis();
        userHis.setHisId(CommonUtil.getSnowID());
        userHis.setId(currentUser.getId());
        userHis.setAccount(currentUser.getAccount());
        userHis.setPassword(currentUser.getPassword());
        userHis.setNickname(currentUser.getNickname());
        userHis.setUserImage(currentUser.getUserImage());
        userHis.setPhoneNumber(currentUser.getPhoneNumber());
        userHis.setEmail(currentUser.getEmail());
        userHis.setType(currentUser.getType());
        userHis.setStatus(currentUser.getStatus());
        userHis.setAuditStatus(currentUser.getAuditStatus());
        userHis.setAuditRemark(currentUser.getAuditRemark());
        userHis.setCreateTime(currentUser.getCreateTime());
        userHis.setUpdateTime(currentUser.getUpdateTime());
        userHis.setIsDeleted(currentUser.getIsDeleted());
        userHis.setHisCreateTime(LocalDateTime.now());

        // 插入历史记录
        int hisRows = otpsUserMapper.insertUserHis(userHis);
        if (hisRows != 1) {
            throw new OtpsBaseException("保存用户历史记录失败");
        }

        // 更新用户信息（只更新非空字段）
        if (reqDto.getNickname() != null) {
            currentUser.setNickname(reqDto.getNickname());
        }
        if (reqDto.getPhoneNumber() != null) {
            // 校验手机号唯一性
            if (!CommonUtil.isEmpty(reqDto.getPhoneNumber())) {
                OtpsUser existPhone = otpsUserMapper.selectByPhoneNumber(reqDto.getPhoneNumber());
                if (existPhone != null && !existPhone.getId().equals(userId)) {
                    throw new OtpsBaseException("该手机号已被使用");
                }
            }
            currentUser.setPhoneNumber(reqDto.getPhoneNumber());
        }
        if (reqDto.getEmail() != null) {
            currentUser.setEmail(reqDto.getEmail());
        }

        // 头像路径校验与旧头像清理
        String oldUserImage = currentUser.getUserImage();
        String newUserImage = reqDto.getUserImage();
        if (!CommonUtil.isEmpty(newUserImage)) {
            if (!fileUtil.fileExists(newUserImage)) {
                throw new OtpsBaseException("头像文件不存在");
            }
            currentUser.setUserImage(newUserImage);
        }

        currentUser.setUpdateTime(LocalDateTime.now());

        // 更新用户信息
        int updateRows = otpsUserMapper.updateById(currentUser);
        if (updateRows != 1) {
            throw new OtpsBaseException("更新用户信息失败");
        }

        // DB 更新成功后再删除旧头像，文件 IO 失败不影响主流程
        if (!CommonUtil.isEmpty(newUserImage)
                && !CommonUtil.isEmpty(oldUserImage)
                && !oldUserImage.equals(newUserImage)) {
            if (!fileUtil.deleteFile(oldUserImage)) {
                log.warn("旧头像删除失败，用户ID: {}, 旧头像路径: {}", userId, oldUserImage);
            }
        }

        UserInfoRespDto userInfoRespDto = new UserInfoRespDto();
        userInfoRespDto.setId(currentUser.getId());
        userInfoRespDto.setAccount(currentUser.getAccount());
        userInfoRespDto.setNickname(currentUser.getNickname());
        userInfoRespDto.setUserImage(currentUser.getUserImage());
        userInfoRespDto.setPhoneNumber(currentUser.getPhoneNumber());
        userInfoRespDto.setEmail(currentUser.getEmail());
        userInfoRespDto.setType(currentUser.getType());
        userInfoRespDto.setStatus(currentUser.getStatus());
        userInfoRespDto.setCreateTime(currentUser.getCreateTime());
        return userInfoRespDto;
    }

    @Override
    public UserInfoRespDto getUserInfo() {
        String userId = CommonUtil.getUserId();
        OtpsUser user = otpsUserMapper.selectById(userId);
        if (user == null) {
            throw new OtpsBaseException("用户不存在");
        }

        UserInfoRespDto userInfoRespDto = new UserInfoRespDto();
        userInfoRespDto.setId(user.getId());
        userInfoRespDto.setAccount(user.getAccount());
        userInfoRespDto.setNickname(user.getNickname());
        userInfoRespDto.setUserImage(user.getUserImage());
        userInfoRespDto.setPhoneNumber(user.getPhoneNumber());
        userInfoRespDto.setEmail(user.getEmail());
        userInfoRespDto.setType(user.getType());
        userInfoRespDto.setStatus(user.getStatus());
        userInfoRespDto.setCreateTime(user.getCreateTime());
        return userInfoRespDto;
    }
}