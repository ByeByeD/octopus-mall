package com.octopusmall.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.octopusmall.business.dto.QueryShopReqDto;
import com.octopusmall.business.dto.ShopRespDto;
import com.octopusmall.business.entity.OtpsShop;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OtpsShopMapper extends BaseMapper<OtpsShop> {

    int insertShop(@Param("shop") OtpsShop shop);

    int updateShop(@Param("shop") OtpsShop shop);

    ShopRespDto getShopById(@Param("id") String id);

    ShopRespDto getShopByUserId(@Param("userId") String userId);

    List<ShopRespDto> listShop(@Param("query") QueryShopReqDto query);

    List<ShopRespDto> listShopByAuditStatus(@Param("auditStatus") String auditStatus);
}
