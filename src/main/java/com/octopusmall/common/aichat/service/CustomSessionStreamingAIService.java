package com.octopusmall.common.aichat.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;

/**
 * 实现记忆管理的流式AI服务
 * LangChain4j 通过 langchain4j-reactor 的 SPI 自动将 Flux<String> 转换为 TokenStream
 */
public interface CustomSessionStreamingAIService {
    TokenStream chat(@MemoryId String sessionId, @UserMessage String message);
}
