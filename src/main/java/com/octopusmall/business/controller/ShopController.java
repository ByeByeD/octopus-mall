package com.octopusmall.business.controller;

import com.octopusmall.business.dto.AddShopReqDto;
import com.octopusmall.business.dto.QueryShopByAuditStatusReqDto;
import com.octopusmall.business.dto.QueryShopByUserInfoReqDto;
import com.octopusmall.business.dto.QueryShopReqDto;
import com.octopusmall.common.global.dto.ResponseDto;
import com.octopusmall.business.dto.ShopRespDto;
import com.octopusmall.business.dto.UpdateShopReqDto;
import com.octopusmall.business.service.OtpsShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/business/shop")
@RequiredArgsConstructor
public class ShopController {

    private final OtpsShopService otpsShopService;

    /**
     * 新增店铺
     */
    @PostMapping("/add")
//    @Permission(name = "shop:add")
    public ResponseDto addShop(@Valid @RequestBody AddShopReqDto req) {
        otpsShopService.addShop(req);
        return new ResponseDto("0", "success");
    }

    /**
     * 修改店铺信息
     */
    @PostMapping("/update")
//    @Permission(name = "shop:update")
    public ResponseDto updateShop(@Valid @RequestBody UpdateShopReqDto req) {
        otpsShopService.updateShop(req);
        return new ResponseDto("0", "success");
    }

    /**
     * 根据审核状态查询店铺列表（审核员接口）
     */
    @PostMapping("/listByAuditStatus")
//    @Permission(name = "shop:listByAuditStatus")
    public ResponseDto listShopByAuditStatus(@Valid @RequestBody QueryShopByAuditStatusReqDto req) {
        List<ShopRespDto> shopList = otpsShopService.listShopByAuditStatus(req.getAuditStatus());
        ResponseDto responseDto = new ResponseDto("0", "success");
        responseDto.setResultData(shopList);
        return responseDto;
    }

    /**
     * 查询当前用户的店铺（商家用户点击"我的小店"时调用）
     */
    @GetMapping("/getMyShop")
//    @Permission(name = "shop:my")
    public ResponseDto getMyShop() {
        ShopRespDto shop = otpsShopService.getMyShop();
        ResponseDto responseDto = new ResponseDto("0", "success");
        responseDto.setResultData(shop);
        return responseDto;
    }

    /**
     * 根据用户账号或手机号查询店铺（管理员接口）
     * 支持通过账号(account)或手机号(phoneNumber)查询用户ID，再查询店铺
     */
    @PostMapping("/getByAccountOrPhoneNumber")
//    @Permission(name = "shop:getByUser")
    public ResponseDto getByAccountOrPhoneNumber(@Valid @RequestBody QueryShopByUserInfoReqDto req) {
        ShopRespDto shop = otpsShopService.getByAccountOrPhoneNumber(req.getAccount(), req.getPhoneNumber());
        ResponseDto responseDto = new ResponseDto("0", "success");
        responseDto.setResultData(shop);
        return responseDto;
    }

    /**
     * 条件查询店铺
     */
    @PostMapping("/getShopByCondition")
//    @Permission(name = "shop:get")
    public ResponseDto getShopByCondition(@RequestBody QueryShopReqDto query) {
        ShopRespDto shop = otpsShopService.getShopByCondition(query);
        ResponseDto responseDto = new ResponseDto("0", "success");
        responseDto.setResultData(shop);
        return responseDto;
    }
}
