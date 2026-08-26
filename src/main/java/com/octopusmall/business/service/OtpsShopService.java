package com.octopusmall.business.service;

import com.octopusmall.business.dto.AddShopReqDto;
import com.octopusmall.business.dto.QueryShopReqDto;
import com.octopusmall.business.dto.ShopRespDto;
import com.octopusmall.business.dto.UpdateShopReqDto;
import java.util.List;

public interface OtpsShopService {

    void addShop(AddShopReqDto req);

    void updateShop(UpdateShopReqDto req);

    ShopRespDto getMyShop();

    ShopRespDto getShopByUserId(String userId);

    ShopRespDto getByAccountOrPhoneNumber(String account, String phoneNumber);

    ShopRespDto getShopByCondition(QueryShopReqDto query);

    List<ShopRespDto> listShopByAuditStatus(String auditStatus);
}
