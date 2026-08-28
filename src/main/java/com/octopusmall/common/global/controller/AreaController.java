package com.octopusmall.common.global.controller;

import com.octopusmall.common.global.dto.AreaReqDto;
import com.octopusmall.common.global.dto.ResponseDto;
import com.octopusmall.common.global.entity.OtpsArea;
import com.octopusmall.common.global.service.OtpsAreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 获取管理地区信息
 */
@RestController
@RequestMapping("/common/area")
@RequiredArgsConstructor
public class AreaController {
    private final OtpsAreaService otpsAreaService;

    /**
     * 根据条件查询地区信息
     * @param areaReqDto
     * @return
     */
    @PostMapping("/getAreaListByCon")
    public ResponseDto getAreaListByCon(@RequestBody AreaReqDto areaReqDto) {
        List<OtpsArea> areaList = otpsAreaService.getAreaListByCon(areaReqDto);
        ResponseDto responseDto = new ResponseDto("0", "success");
        responseDto.setResultData(areaList);
        return responseDto;
    }
}
