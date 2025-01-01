package com.bankapp.messagerouter.controller;

import com.bankapp.messagerouter.entity.Message;
import com.bankapp.messagerouter.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MessageController {

    private final MessageService messageService;
    private final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

//    @GetMapping("/messages")
//    public ResponseEntity<List<Message>> getAllMessages() {
//        List<Message> messages = messageService.getAllMessages();
//        return ResponseEntity.ok(messages);
//    }


    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }



    @GetMapping("/message/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        Message message = messageService.getMessageById(id);
        return ResponseEntity.ok(message);
    }



    @PostMapping("/message")
    public ResponseEntity<Message> saveMessage(@RequestBody Message message) {
        Message savedMessage = messageService.saveMessage(message);
        return new ResponseEntity<>(savedMessage, HttpStatus.CREATED);
    }



    @DeleteMapping("/message/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        Optional<Message> message = messageService.findById(id);
        if (message.isPresent()) {
            messageService.deleteMessage(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }










}
