package com.yunuskaya.task.management.api.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class TaskWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public TaskWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public String broadcastMessage(String message) {
        return message;
    }
}
