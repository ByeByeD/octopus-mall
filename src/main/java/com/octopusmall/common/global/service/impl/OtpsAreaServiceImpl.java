package com.octopusmall.common.global.service.impl;

import com.octopusmall.common.global.dto.AreaReqDto;
import com.octopusmall.common.global.entity.OtpsArea;
import com.octopusmall.common.global.mapper.OtpsAreaMapper;
import com.octopusmall.common.global.service.OtpsAreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OtpsAreaServiceImpl implements OtpsAreaService {
    private final OtpsAreaMapper otpsAreaMapper;
    @Override
    public List<OtpsArea> getAreaListByCon(AreaReqDto areaReqDto) {
        return otpsAreaMapper.getAreaListByCon(areaReqDto);
    }
}
