package com.octopusmall.common.global.service;

import com.octopusmall.common.global.entity.OtpsDict;

import java.util.List;

public interface OtpsDictService {
    List<OtpsDict> getDictInfosById(String dictId);
}
