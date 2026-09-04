package com.octopusmall.common.aichat.service;

import com.octopusmall.common.aichat.dto.MessageRespDto;
import com.octopusmall.common.aichat.dto.SessionRespDto;
import java.util.List;

public interface AIChatSessionService {
    
    /**
     * 获取用户会话列表（按更新时间倒序）
     */
    List<SessionRespDto> getUserSessions(String userId);
    
    /**
     * 获取会话消息列表（按msgIndex升序）
     */
    List<MessageRespDto> getSessionMessages(String sessionId);
    
    /**
     * 更新会话名称
     */
    void updateSessionName(String sessionId, String userId, String name);
    
    /**
     * 删除会话（级联删除消息）
     */
    void deleteSession(String sessionId, String userId);
}
