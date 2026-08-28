package com.octopusmall.common.global.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.octopusmall.common.global.entity.OtpsDict;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OtpsDictMapper extends BaseMapper<OtpsDict> {
    List<OtpsDict> getDictInfosById(@Param("dictId") String dictId);
}
