package com.octopusmall.common.aichat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.octopusmall.common.aichat.dto.MessageRespDto;
import com.octopusmall.common.aichat.dto.SessionRespDto;
import com.octopusmall.common.aichat.entity.OtpsChatMessage;
import com.octopusmall.common.aichat.entity.OtpsChatSession;
import com.octopusmall.common.aichat.mapper.OtpsChatMessageMapper;
import com.octopusmall.common.aichat.mapper.OtpsChatSessionMapper;
import com.octopusmall.common.aichat.service.AIChatSessionService;
import com.octopusmall.common.exception.OtpsBaseException;
import com.octopusmall.common.util.CommonUtil;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AIChatSessionServiceImpl implements AIChatSessionService {

    private final OtpsChatSessionMapper sessionMapper;
    private final OtpsChatMessageMapper messageMapper;

    @Override
    public List<SessionRespDto> getUserSessions(String userId) {
        LambdaQueryWrapper<OtpsChatSession> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OtpsChatSession::getUserId, userId)
               .orderByDesc(OtpsChatSession::getUpdateTime);
        
        List<OtpsChatSession> sessions = sessionMapper.selectList(wrapper);
        
        return sessions.stream()
                .map(this::convertToSessionDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageRespDto> getSessionMessages(String sessionId) {
        LambdaQueryWrapper<OtpsChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OtpsChatMessage::getSessionId, sessionId)
               .orderByAsc(OtpsChatMessage::getMsgIndex);
        
        List<OtpsChatMessage> messages = messageMapper.selectList(wrapper);
        
        return messages.stream()
                .map(this::convertToMessageDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSessionName(String sessionId, String userId, String name) {
        // 业务校验：会话归属权
        LambdaQueryWrapper<OtpsChatSession> query = new LambdaQueryWrapper<>();
        query.eq(OtpsChatSession::getId, sessionId)
             .eq(OtpsChatSession::getUserId, userId);
        OtpsChatSession session = sessionMapper.selectOne(query);
        
        if (session == null) {
            throw new OtpsBaseException("会话不存在或无权限操作");
        }
        
        // 更新名称和更新时间
        LambdaUpdateWrapper<OtpsChatSession> update = new LambdaUpdateWrapper<>();
        update.eq(OtpsChatSession::getId, sessionId)
              .set(OtpsChatSession::getName, name)
              .set(OtpsChatSession::getUpdateTime, LocalDateTime.now());
        sessionMapper.update(null, update);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSession(String sessionId, String userId) {
        // 业务校验：会话归属权
        LambdaQueryWrapper<OtpsChatSession> query = new LambdaQueryWrapper<>();
        query.eq(OtpsChatSession::getId, sessionId)
             .eq(OtpsChatSession::getUserId, userId);
        OtpsChatSession session = sessionMapper.selectOne(query);
        
        if (session == null) {
            throw new OtpsBaseException("会话不存在或无权限操作");
        }
        
        // 1. 删除关联消息
        LambdaQueryWrapper<OtpsChatMessage> msgQuery = new LambdaQueryWrapper<>();
        msgQuery.eq(OtpsChatMessage::getSessionId, sessionId);
        messageMapper.delete(msgQuery);
        
        // 2. 删除会话
        sessionMapper.deleteById(sessionId);
    }

    // 实体转DTO
    private SessionRespDto convertToSessionDto(OtpsChatSession session) {
        SessionRespDto dto = new SessionRespDto();
        dto.setId(session.getId());
        dto.setName(session.getName());
        dto.setCreatTime(session.getCreateTime());
        dto.setUpdateTime(session.getUpdateTime());
        return dto;
    }

    private MessageRespDto convertToMessageDto(OtpsChatMessage message) {
        MessageRespDto dto = new MessageRespDto();
        dto.setId(message.getId());
        dto.setMsgIndex(message.getMsgIndex());
        dto.setSessionId(message.getSessionId());
        this.parseMsgContent(message.getMsgContent(), dto);
        dto.setCreateTime(message.getCreateTime());
        return dto;
    }

    private void parseMsgContent(String msgContent,MessageRespDto messageRespDto) {
        if (CommonUtil.isEmpty(msgContent)) {
            return;
        }
        ChatMessage chatMessage = ChatMessageDeserializer.messageFromJson(msgContent);
        switch (chatMessage.type()) {
            case USER -> {
                UserMessage userMessage = (UserMessage) chatMessage;
                TextContent content = (TextContent) userMessage.contents().get(0);
                messageRespDto.setMsgContent(content.text());
                messageRespDto.setType(userMessage.type().name());
            }
            case AI -> {
                AiMessage aiMessage = (AiMessage) chatMessage;
                messageRespDto.setMsgContent(aiMessage.text());
                messageRespDto.setType(aiMessage.type().name());
            }
            case SYSTEM -> {
                SystemMessage systemMessage = (SystemMessage) chatMessage;
                messageRespDto.setMsgContent(systemMessage.text());
                messageRespDto.setType(systemMessage.type().name());
            }
            default -> {}
        }
    }
}
