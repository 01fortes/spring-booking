package com.jsamkt.learn.booking.controller;

import com.jsamkt.learn.booking.dto.ChatUserMessage;
import com.jsamkt.learn.booking.service.OpenAiService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import reactor.core.scheduler.Scheduler;


@RestController
@RequestMapping("/chat")
public class ChatController {

    private final OpenAiService service;
    private final Scheduler scheduler;

    public ChatController(OpenAiService service,
                          @Qualifier("openAiRunScheduler") Scheduler scheduler) {
        this.service = service;
        this.scheduler = scheduler;
    }

    @PostMapping("/")
    public Mono<ResponseEntity<String>> chat(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody ChatUserMessage message) {
        return Mono.create((MonoSink<ResponseEntity<String>> sink) ->
                sink.success(ResponseEntity.ok(
                        service.handleMessages(userId, message)
                ))
        ).subscribeOn(scheduler);
    }
}



















