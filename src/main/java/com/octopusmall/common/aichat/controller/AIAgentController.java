package com.octopusmall.common.aichat.controller;

import com.octopusmall.common.aichat.dto.ChatSessionReqDto;
import com.octopusmall.common.aichat.dto.MessageRespDto;
import com.octopusmall.common.aichat.dto.SessionRespDto;
import com.octopusmall.common.aichat.dto.UpdateSessionNameReqDto;
import com.octopusmall.common.aichat.service.AIChatSessionService;
import com.octopusmall.common.aichat.service.CustomAIService;
import com.octopusmall.common.aichat.service.CustomSessionAIService;
import com.octopusmall.common.aichat.service.CustomSessionStreamingAIService;
import com.octopusmall.common.exception.OtpsBaseException;
import com.octopusmall.common.global.dto.ResponseDto;
import com.octopusmall.common.util.CommonUtil;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.PartialThinking;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.tool.ToolExecution;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/common/aiChat")
@RequiredArgsConstructor
public class AIAgentController {
    public final OpenAiChatModel openAiChatModel;

    public final CustomAIService customAIService;

    private final CustomSessionAIService customSessionAIService;

    private final CustomSessionStreamingAIService customSessionStreamingAIService;

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
     * 实现完全会话管理的流式响应接口
     * 关键设计：使用 Spring MVC 原生的 SseEmitter（org.springframework.web.servlet.mvc.method.annotation.SseEmitter），
     * 它会自动设置 Content-Type: text/event-stream，并在每个 send() 调用时立即 flush 到客户端。
     * 这是 Spring MVC 6.x 中处理 SSE 流式响应的官方推荐方式，不需要任何额外的 HttpMessageConverter。
     *
     * @param requestMap 包含 message 和 sessionId
     * @return SseEmitter 通过 event-stream 协议逐字推送 AI token
     */
    @PostMapping(value = "/business2SessionFlux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter business2SessionFlux(@RequestBody HashMap<String, Object> requestMap, HttpServletResponse response) {
        String message = (String) requestMap.get("message");
        String sessionId = (String) requestMap.get("sessionId");

        if (CommonUtil.isEmpty(message)) {
            throw new OtpsBaseException("message cannot empty");
        }
        if (CommonUtil.isEmpty(sessionId)) {
            throw new OtpsBaseException("sessionId cannot empty");
        }
        // 关闭Tomcat缓存
        response.setBufferSize(0);

        // 1. 创建 SseEmitter，0 表示永不超时（也可指定毫秒数）
        SseEmitter emitter = new SseEmitter(180_000L);

        // 2. 在独立线程中订阅 Flux 并通过 emitter 推送每个 token
        TokenStream tokenStream = customSessionStreamingAIService.chat(sessionId, message);

        tokenStream
                .onPartialResponse((String partialResponse) -> {
                    try {
                        // 输出普通内容token
                        emitter.send(SseEmitter.event()
                                .name("content")
                                .data(partialResponse));
                        // 必须要，否则刷不出去
                        // 强制刷新输出流！！核心代码
                        response.getOutputStream().flush();
                    } catch (Exception e) {
                        emitter.completeWithError(e);
                    }
                })
                .onPartialThinking((PartialThinking partialThinking) -> {
                    try {
                        // 输出推理链内容（deepseek / o1 思考内容）
                        emitter.send(SseEmitter.event()
                                .name("thinking")
                                .data(partialThinking.text()));
                        // 强制刷新输出流！！核心代码
                        response.getOutputStream().flush();
                    } catch (Exception e) {
                        emitter.completeWithError(e);
                    }
                })
                .onRetrieved((List<Content> contents) -> {
                    try {
                        // RAG检索到的文档
                        emitter.send(SseEmitter.event()
                                .name("retrieved")
                                .data(contents));
                        // 强制刷新输出流！！核心代码
                        response.getOutputStream().flush();
                    } catch (Exception e) {
                        emitter.completeWithError(e);
                    }
                })
                .beforeToolExecution((beforeToolExecution) -> {
                    try {
                        // 调用工具之前
                        emitter.send(SseEmitter.event()
                                .name("beforeTool")
                                .data(beforeToolExecution));
                        // 强制刷新输出流！！核心代码
                        response.getOutputStream().flush();
                    } catch (Exception e) {
                        emitter.completeWithError(e);
                    }
                })
                .onToolExecuted((ToolExecution toolExecution) -> {
                    try {
                        // 工具调用完成
                        emitter.send(SseEmitter.event()
                                .name("toolExecuted")
                                .data(toolExecution));
                        // 强制刷新输出流！！核心代码
                        response.getOutputStream().flush();
                    } catch (Exception e) {
                        emitter.completeWithError(e);
                    }
                })
                .onCompleteResponse((ChatResponse chatResponse) -> {
                    emitter.complete();
                })
                .onError(emitter::completeWithError)
                .start(); // ⚠️ 必须调用 .start() 才会真正发起LLM请求

        return emitter;
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
