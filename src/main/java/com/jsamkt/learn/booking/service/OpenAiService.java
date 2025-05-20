package com.jsamkt.learn.booking.service;

import com.jsamkt.learn.booking.dto.ChatUserMessage;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OpenAiService {

    private final ConversationIdService conversationIdService;
    private final ChatClient chatClient;

    public OpenAiService(ConversationIdService conversationIdService, ChatClient chatClient) {
        this.conversationIdService = conversationIdService;
        this.chatClient = chatClient;
    }

    public String handleMessages(String userId, ChatUserMessage message) {
        return chatClient.prompt()
                .user(message.getMessage())
                .advisors(conversationIdService.getAdviser(userId))
                .call()
                .content();
    }
}
