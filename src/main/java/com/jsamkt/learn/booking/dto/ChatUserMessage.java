package com.jsamkt.learn.booking.dto;

public class ChatUserMessage {
    private final String message;

    public ChatUserMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
