package com.mnnu.examine.modules.sys.controller;

import com.google.gson.Gson;
import com.mnnu.examine.common.exception.MessageException;
import com.mnnu.examine.modules.sys.entity.ChatMessageEntity;
import com.mnnu.examine.modules.sys.entity.UserEntity;
import com.mnnu.examine.modules.sys.service.ChatMessageService;
import com.mnnu.examine.modules.sys.vo.MessageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 聊天控制器
 *
 * @author qiaoh
 * @date 2021/12/08
 */
@Controller
public class ChatController {
    ChatMessageService chatMessageService;

    SimpMessagingTemplate template;

    Gson gson;

    @Autowired
    public ChatController(ChatMessageService chatMessageService, SimpMessagingTemplate template, Gson gson) {
        this.chatMessageService = chatMessageService;
        this.template = template;
        this.gson = gson;
    }


    /**
     * 聊天处理
     *
     * @param message 聊天信息
     */
    @MessageMapping("/chat/private")
    public void handleChat(@Payload @Validated MessageVO message) {
        if (message.getToUser() <= 0) {
            throw new MessageException("消息接收方不存在");
        }
        // 不可发送给自己
        Long from = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        if (from == null && from.equals(message.getToUser())) {
            throw new MessageException("消息不可发送给自己");
        }
        message.setSendTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        ChatMessageEntity messageEntity = new ChatMessageEntity();

        BeanUtils.copyProperties(message, messageEntity);
        messageEntity.setFromUser(from);

        chatMessageService.save(messageEntity);

        template.convertAndSend("/message/" + messageEntity.getToUser(), gson.toJson(messageEntity));
    }

}
