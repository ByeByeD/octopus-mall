package com.octopusmall.common.aichat.configuration;

import com.octopusmall.common.aichat.service.CustomAIService;
import com.octopusmall.common.aichat.service.CustomChatMemoryStore;
import com.octopusmall.common.aichat.service.CustomSessionAIService;
import com.octopusmall.common.aichat.service.CustomSessionStreamingAIService;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.openai.OpenAiTokenCountEstimator;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
public class AIAgentConfiguration {
    @Value("${LLM-BASE-URL.BAI-LIAN}")
    private String baseUrl;

    private final CustomChatMemoryStore customChatMemoryStore;

//    @PostConstruct
//    public void testLoadPromptFile() throws IOException {
//        try (InputStream is = getClass().getClassLoader().getResourceAsStream("systemPrompt.md")) {
//            if(is == null){
//                System.err.println("==== 文件找不到！inputStream = null ====");
//                return;
//            }
//            String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
//            System.out.println("=====成功读取md内容=====\n" + content);
//        }
//    }

    @Bean
    public OpenAiChatModel getOpenAiChatModel() {
        OpenAiChatModel openAiChatModel = OpenAiChatModel.builder()
                .baseUrl(baseUrl)
                .apiKey(System.getenv("BAILIAN_API_KEY"))
                .modelName("qwen3.7-plus")
                .logRequests(true)
                .logResponses(true)
                .build();
        return openAiChatModel;
    }

    @Bean
    public OpenAiStreamingChatModel getOpenAiStreamingChatModel() {
        OpenAiStreamingChatModel openAiChatModel = OpenAiStreamingChatModel.builder()
                .baseUrl(baseUrl)
                .apiKey(System.getenv("BAILIAN_API_KEY"))
                .modelName("qwen3.7-plus")
                .logRequests(true)
                .logResponses(true)
                .build();
        return openAiChatModel;
    }

    /**
     * 固定死的写法
     * @param openAiChatModel
     * @return
     */
    @Bean
    public CustomAIService getCustomAIService(OpenAiChatModel openAiChatModel) {
        // 直接在入参上定义，会自动注入依赖，前提是这个OpenAiChatModel需要被spring管理
//        OpenAiChatModel openAiChatModel = this.getOpenAiChatModel();
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .id("test111")
                .maxMessages(20)
                .chatMemoryStore(customChatMemoryStore)
                .build();
        CustomAIService customAIService = AiServices.builder(CustomAIService.class)
                .chatModel(openAiChatModel)
                .chatMemory(chatMemory)
                .build();
        return customAIService;
    }

    /**
     * 会话工厂：根据传入的会话id动态创建ChatMemory
     */
    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> TokenWindowChatMemory.builder()
                .id(memoryId)   // 动态会话ID，由调用方传入
                .maxTokens(3000, new OpenAiTokenCountEstimator("gpt-5"))
                .chatMemoryStore(customChatMemoryStore)
                .build();
    }

    @Bean
    public CustomSessionAIService getCustomSessionAIService(OpenAiChatModel openAiChatModel, ChatMemoryProvider chatMemoryProvider) {
        String systemPromptText;
        try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream("systemPrompt.md")){
            if(inputStream == null){
                throw new RuntimeException("systemPrompt.md 文件缺失，请检查classpath资源");
            }
            systemPromptText = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("读取systemPrompt.md失败",e);
        }

        CustomSessionAIService customSessionAIService = AiServices.builder(CustomSessionAIService.class)
                .chatModel(openAiChatModel)
                .chatMemoryProvider(chatMemoryProvider)
                .systemMessageProvider(id -> systemPromptText)
                .build();
        return customSessionAIService;
    }

    /**
     * 流式调用AI Service
     * @param openAiStreamingChatModel
     * @param chatMemoryProvider
     * @return
     */
    @Bean
    public CustomSessionStreamingAIService getCustomSessionStreamingAIService(OpenAiStreamingChatModel openAiStreamingChatModel, ChatMemoryProvider chatMemoryProvider) {
        CustomSessionStreamingAIService streamingAIService = AiServices.builder(CustomSessionStreamingAIService.class)
                .streamingChatModel(openAiStreamingChatModel)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
        return streamingAIService;
    }
}
