package com.octopusmall.common.aichat.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.octopusmall.common.aichat.entity.OtpsChatMessage;
import com.octopusmall.common.aichat.entity.OtpsChatSession;
import com.octopusmall.common.aichat.mapper.OtpsChatMessageMapper;
import com.octopusmall.common.aichat.mapper.OtpsChatSessionMapper;
import com.octopusmall.common.util.CommonUtil;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomChatMemoryStore implements ChatMemoryStore {
    private final OtpsChatSessionMapper otpsChatSessionMapper;
    private final OtpsChatMessageMapper otpsChatMessageMapper;

    @Override
    public List<ChatMessage> getMessages(Object o) {
        QueryWrapper<OtpsChatMessage> otpsChatMessageQueryWrapper = new QueryWrapper<>();
        otpsChatMessageQueryWrapper.eq("session_id", o)
                .orderByAsc("msg_index");
        List<OtpsChatMessage> otpsChatMessages = otpsChatMessageMapper.selectList(otpsChatMessageQueryWrapper);
        ArrayList<ChatMessage> chatMessages = new ArrayList<>();
        for (OtpsChatMessage otpsChatMessage: otpsChatMessages) {
            ChatMessage chatMessage = ChatMessageDeserializer.messageFromJson(otpsChatMessage.getMsgContent());
            chatMessages.add(chatMessage);
        }
        return chatMessages;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMessages(Object o, List<ChatMessage> list) {
        QueryWrapper<OtpsChatSession> otpsChatSessionQueryWrapper = new QueryWrapper<>();
        otpsChatSessionQueryWrapper.eq("id", o);
        boolean exists = otpsChatSessionMapper.exists(otpsChatSessionQueryWrapper);
        // 不存在会话表先插入会话表
        if (!exists) {
            UserMessage userMessage = null;
            for (ChatMessage chatMessage:list) {
                if (chatMessage.type() == ChatMessageType.USER) {
                    userMessage = (UserMessage) chatMessage;
                    break;
                }
            }
            if (userMessage == null) {
                return;
            }
            OtpsChatSession otpsChatSession = new OtpsChatSession();
            otpsChatSession.setId((String) o);

            String sessionName = "新对话";
            List<Content> contents = userMessage.contents();
            if (contents.get(0) instanceof TextContent) {
                TextContent textContent = (TextContent) contents.get(0);
                String text = textContent.text();
                if (text.length() > 20) {
                    sessionName = text.substring(0, 20);
                }
                else {
                    sessionName = text;
                }
            }
            otpsChatSession.setName(sessionName);
            otpsChatSession.setUserId(CommonUtil.getUserId());
            otpsChatSessionMapper.insert(otpsChatSession);
        }
        // 1.先清空会话详情表会话数据
        QueryWrapper<OtpsChatMessage> otpsChatMessageQueryWrapper = new QueryWrapper<>();
        otpsChatMessageQueryWrapper.eq("session_id", o);
        otpsChatMessageMapper.delete(otpsChatMessageQueryWrapper);
        // 2.插入数据到会话详情表
        ArrayList<OtpsChatMessage> otpsChatMessages = new ArrayList<>();
        for (int i=0; i<list.size(); i++) {
            OtpsChatMessage otpsChatMessage = new OtpsChatMessage();
            otpsChatMessage.setId(CommonUtil.getSnowID());
            otpsChatMessage.setSessionId((String) o);
            otpsChatMessage.setMsgIndex(i);
            otpsChatMessage.setMsgContent(ChatMessageSerializer.messageToJson(list.get(i)));
            otpsChatMessages.add(otpsChatMessage);
        }
        otpsChatMessageMapper.insert(otpsChatMessages);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMessages(Object o) {
        QueryWrapper<OtpsChatMessage> otpsChatMessageQueryWrapper = new QueryWrapper<>();
        otpsChatMessageQueryWrapper.eq("session_id", o);
        otpsChatMessageMapper.delete(otpsChatMessageQueryWrapper);
        otpsChatSessionMapper.deleteById((String)o);
    }
}
