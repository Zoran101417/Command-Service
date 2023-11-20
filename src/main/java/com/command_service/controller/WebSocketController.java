package com.command_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class WebSocketController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public ResponseEntity greeting(String message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return ResponseEntity.status(HttpStatus.OK).body("Hello, " + message + "!");
    }
}
