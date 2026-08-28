package com.octopusmall.business.service.impl;

import com.octopusmall.business.service.OtpsShopService;
import com.octopusmall.common.exception.OtpsBaseException;
import com.octopusmall.business.dto.AddShopReqDto;
import com.octopusmall.business.dto.QueryShopReqDto;
import com.octopusmall.business.dto.ShopRespDto;
import com.octopusmall.business.dto.UpdateShopReqDto;
import com.octopusmall.business.entity.OtpsShop;
import com.octopusmall.common.global.entity.OtpsUser;
import com.octopusmall.business.mapper.OtpsShopMapper;
import com.octopusmall.common.global.mapper.OtpsUserMapper;
import com.octopusmall.common.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OtpsShopServiceImpl implements OtpsShopService {

    private final OtpsShopMapper otpsShopMapper;
    private final OtpsUserMapper otpsUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addShop(AddShopReqDto req) {
        String userId = CommonUtil.getUserId();

        ShopRespDto existingShop = otpsShopMapper.getShopByUserId(userId);
        if (existingShop != null) {
            throw new OtpsBaseException("该用户已存在店铺，无需重复创建");
        }

        OtpsShop shop = new OtpsShop();
        shop.setId(CommonUtil.getSnowID());
        shop.setUserId(userId);
        shop.setShopName(req.getShopName());
        shop.setProvince(req.getProvince());
        shop.setCity(req.getCity());
        shop.setDetailAddress(req.getDetailAddress());
        shop.setCategory(req.getCategory());
        shop.setDescription(req.getDescription());
        shop.setAvatarUrl(req.getAvatarUrl());
        shop.setContactName(req.getContactName());
        shop.setContactPhone(req.getContactPhone());
        shop.setLicenseNo(req.getLicenseNo());
        shop.setLicensePhotoUrl(req.getLicensePhotoUrl());
        shop.setLicenseExpireDate(req.getLicenseExpireDate());
        shop.setAuditStatus("wait");
        shop.setShopStatus("normal");
        shop.setCreateTime(LocalDateTime.now());
        shop.setUpdateTime(LocalDateTime.now());

        otpsShopMapper.insertShop(shop);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShop(UpdateShopReqDto req) {
        String userId = CommonUtil.getUserId();

        ShopRespDto myShop = otpsShopMapper.getShopByUserId(userId);
        if (myShop == null) {
            throw new OtpsBaseException("店铺不存在");
        }

        OtpsShop shop = new OtpsShop();
        shop.setId(myShop.getId());
        shop.setUserId(userId);
        shop.setShopName(req.getShopName());
        shop.setProvince(req.getProvince());
        shop.setCity(req.getCity());
        shop.setDetailAddress(req.getDetailAddress());
        shop.setCategory(req.getCategory());
        shop.setDescription(req.getDescription());
        shop.setAvatarUrl(req.getAvatarUrl());
        shop.setContactName(req.getContactName());
        shop.setContactPhone(req.getContactPhone());
        shop.setLicenseNo(req.getLicenseNo());
        shop.setLicensePhotoUrl(req.getLicensePhotoUrl());
        shop.setLicenseExpireDate(req.getLicenseExpireDate());
        shop.setUpdateTime(LocalDateTime.now());
        shop.setShopStatus(req.getShopStatus());

        otpsShopMapper.updateShop(shop);
    }

    @Override
    public ShopRespDto getMyShop() {
        String userId = CommonUtil.getUserId();
        ShopRespDto shop = otpsShopMapper.getShopByUserId(userId);
        if (shop == null) {
            throw new OtpsBaseException("您还没有创建店铺");
        }
        return shop;
    }

    @Override
    public List<ShopRespDto> listShopByAuditStatus(String auditStatus) {
        return otpsShopMapper.listShopByAuditStatus(auditStatus);
    }

    @Override
    public ShopRespDto getShopByUserId(String userId) {
        ShopRespDto shop = otpsShopMapper.getShopByUserId(userId);
        if (shop == null) {
            throw new OtpsBaseException("店铺不存在");
        }
        return shop;
    }

    @Override
    public ShopRespDto getByAccountOrPhoneNumber(String account, String phoneNumber) {
        OtpsUser user = null;

        if (!CommonUtil.isEmpty(account)) {
            user = otpsUserMapper.selectByAccount(account);
        } else if (!CommonUtil.isEmpty(phoneNumber)) {
            user = otpsUserMapper.selectByPhoneNumber(phoneNumber);
        }

        if (user == null) {
            throw new OtpsBaseException("用户不存在");
        }

        return getShopByUserId(user.getId());
    }

    @Override
    public ShopRespDto getShopByCondition(QueryShopReqDto query) {
        List<ShopRespDto> shopList = otpsShopMapper.listShop(query);
        if (shopList == null || shopList.isEmpty()) {
            throw new OtpsBaseException("店铺不存在");
        }
        return shopList.get(0);
    }
}
