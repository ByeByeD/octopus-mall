package com.octopusmall.common.aichat.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * 实现记忆管理，可传入会话id
 */
public interface CustomSessionAIService {
//    目前注解方式未生效
//    @SystemMessage(fromResource = "systemPrompt.md")
    String chat(@MemoryId String sessionId, @UserMessage String message);
}
