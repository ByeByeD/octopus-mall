package com.octopusmall.common.global.service.impl;

import com.octopusmall.common.global.entity.OtpsDict;
import com.octopusmall.common.global.mapper.OtpsDictMapper;
import com.octopusmall.common.global.service.OtpsDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OtpsDictServiceImpl implements OtpsDictService {
    private final OtpsDictMapper otpsDictMapper;
    @Override
    public List<OtpsDict> getDictInfosById(String dictId) {
        return otpsDictMapper.getDictInfosById(dictId);
    }
}
