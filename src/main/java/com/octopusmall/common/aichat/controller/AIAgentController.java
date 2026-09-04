package com.octopusmall.common.aichat.controller;

import com.octopusmall.common.aichat.dto.ChatSessionReqDto;
import com.octopusmall.common.aichat.dto.MessageRespDto;
import com.octopusmall.common.aichat.dto.SessionRespDto;
import com.octopusmall.common.aichat.dto.UpdateSessionNameReqDto;
import com.octopusmall.common.aichat.service.AIChatSessionService;
import com.octopusmall.common.aichat.service.CustomAIService;
import com.octopusmall.common.aichat.service.CustomSessionAIService;
import com.octopusmall.common.exception.OtpsBaseException;
import com.octopusmall.common.global.dto.ResponseDto;
import com.octopusmall.common.util.CommonUtil;
import dev.langchain4j.model.openai.OpenAiChatModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/common/aiChat")
@RequiredArgsConstructor
public class AIAgentController {
    public final OpenAiChatModel openAiChatModel;

    public final CustomAIService customAIService;

    private final CustomSessionAIService customSessionAIService;

    private final AIChatSessionService aiChatSessionService;

    @PostMapping("/business")
    public ResponseDto businessToChat(@RequestBody HashMap<String, Object> requestMap) {
        String message = (String) requestMap.get("message");

        String result = openAiChatModel.chat(message);
        ResponseDto success = ResponseDto.success();
        success.setResultData(result);
        return success;
    }

    /**
     * 实现会话记忆，但是会话记忆仅保留在服务器端，一但应用关闭那么将丢失记忆
     * @param requestMap
     * @return
     */
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

    /**
     * 获取当前用户的会话列表
     * GET /common/aiChat/getSessions
     */
    @GetMapping("/getSessions")
    public ResponseDto getSessions() {
        String userId = CommonUtil.getUserId();
        if (CommonUtil.isEmpty(userId)) {
            throw new OtpsBaseException("用户未登录");
        }
        
        List<SessionRespDto> sessions = aiChatSessionService.getUserSessions(userId);
        
        ResponseDto responseDto = new ResponseDto("0", "success");
        responseDto.setResultData(sessions);
        return responseDto;
    }

    /**
     * 获取会话的消息列表
     * POST /common/aiChat/getMessages
     */
    @PostMapping("/getMessages")
    public ResponseDto getMessages(@Valid @RequestBody ChatSessionReqDto reqDto) {
        List<MessageRespDto> messages = aiChatSessionService.getSessionMessages(reqDto.getSessionId());
        
        ResponseDto responseDto = new ResponseDto("0", "success");
        responseDto.setResultData(messages);
        return responseDto;
    }

    /**
     * 更新会话名称
     * POST /common/aiChat/updateSessionName
     */
    @PostMapping("/updateSessionName")
    public ResponseDto updateSessionName(@Valid @RequestBody UpdateSessionNameReqDto reqDto) {
        String userId = CommonUtil.getUserId();
        if (CommonUtil.isEmpty(userId)) {
            throw new OtpsBaseException("用户未登录");
        }
        
        aiChatSessionService.updateSessionName(reqDto.getSessionId(), userId, reqDto.getName());
        
        return new ResponseDto("0", "success");
    }

    /**
     * 删除会话（级联删除消息）
     * POST /common/aiChat/deleteSession
     */
    @PostMapping("/deleteSession")
    public ResponseDto deleteSession(@Valid @RequestBody ChatSessionReqDto reqDto) {
        String userId = CommonUtil.getUserId();
        if (CommonUtil.isEmpty(userId)) {
            throw new OtpsBaseException("用户未登录");
        }
        
        aiChatSessionService.deleteSession(reqDto.getSessionId(), userId);
        
        return new ResponseDto("0", "success");
    }
}
