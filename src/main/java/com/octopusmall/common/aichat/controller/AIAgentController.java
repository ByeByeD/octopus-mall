package com.octopusmall.common.aichat.controller;

import com.octopusmall.common.aichat.service.CustomAIService;
import com.octopusmall.common.aichat.service.CustomSessionAIService;
import com.octopusmall.common.exception.OtpsBaseException;
import com.octopusmall.common.global.dto.ResponseDto;
import com.octopusmall.common.util.CommonUtil;
import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/common/aiChat")
@RequiredArgsConstructor
public class AIAgentController {
    public final OpenAiChatModel openAiChatModel;

    public final CustomAIService customAIService;

    private final CustomSessionAIService customSessionAIService;

    @PostMapping("/business")
    public ResponseDto businessToChat(@RequestBody HashMap<String, Object> requestMap) {
        String message = (String) requestMap.get("message");

        String result = openAiChatModel.chat(message);
        ResponseDto success = ResponseDto.success();
        success.setResultData(result);
        return success;
    }

    @PostMapping("/business2Memory")
    public ResponseDto business2Memory(@RequestBody HashMap<String, Object> requestMap) {
        String message = (String) requestMap.get("message");

        String chat = customAIService.chat(message);
        ResponseDto success = ResponseDto.success();
        success.setResultData(chat);
        return success;
    }

    /**
     * 实现完全会话管理，会话信息记录到oracle
     * @param requestMap
     * @return
     */
    @PostMapping("/business2Session")
    public ResponseDto business2Session(@RequestBody HashMap<String, Object> requestMap) {
        String message = (String) requestMap.get("message");
        String sessionId = (String) requestMap.get("sessionId");
        if (CommonUtil.isEmpty(message)) {
            throw new OtpsBaseException("message cannot empty");
        }
        if (CommonUtil.isEmpty(sessionId)) {
            throw new OtpsBaseException("sessionId cannot empty");
        }

        String chat = customSessionAIService.chat(sessionId, message);
        ResponseDto success = ResponseDto.success();
        success.setResultData(chat);
        return success;
    }
}
