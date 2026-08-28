package com.octopusmall.common.global.controller;

import com.octopusmall.common.exception.OtpsBaseException;
import com.octopusmall.common.global.dto.ResponseDto;
import com.octopusmall.common.global.entity.OtpsDict;
import com.octopusmall.common.global.service.OtpsDictService;
import com.octopusmall.common.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/common/dict")
public class DictController {
    private final OtpsDictService otpsDictService;

    @PostMapping("/getDictInfosById")
    public ResponseDto getDictInfosById(String dictId) {
        if (CommonUtil.isEmpty(dictId)) {
            throw new OtpsBaseException("dictId 不能为空");
        }
        List<OtpsDict> dictInfoById = otpsDictService.getDictInfosById(dictId);
        ResponseDto responseDto = new ResponseDto("0", "success");
        responseDto.setResultData(dictInfoById);
        return responseDto;
    }
}
