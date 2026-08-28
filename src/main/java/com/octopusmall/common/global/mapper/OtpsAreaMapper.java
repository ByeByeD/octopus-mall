package com.octopusmall.common.global.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.octopusmall.common.global.dto.AreaReqDto;
import com.octopusmall.common.global.entity.OtpsArea;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OtpsAreaMapper extends BaseMapper<OtpsArea> {

    List<OtpsArea> getAreaListByCon(@Param("areaReqDto") AreaReqDto areaReqDto);
}
