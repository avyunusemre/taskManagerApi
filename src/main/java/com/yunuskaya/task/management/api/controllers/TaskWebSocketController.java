package com.yunuskaya.task.management.api.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class TaskWebSocketController {

    @MessageMapping("/task-updates")
    @SendToUser("/queue/reply")
    public String handleTaskUpdate(String message) {
        return "Task Update: " + message;
    }
}
