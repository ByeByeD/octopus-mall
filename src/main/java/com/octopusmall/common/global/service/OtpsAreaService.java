package com.octopusmall.common.global.service;

import com.octopusmall.common.global.dto.AreaReqDto;
import com.octopusmall.common.global.entity.OtpsArea;

import java.util.List;

public interface OtpsAreaService {
    List<OtpsArea> getAreaListByCon(AreaReqDto areaReqDto);
}
