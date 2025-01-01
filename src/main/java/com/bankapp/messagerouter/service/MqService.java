package com.bankapp.messagerouter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Queue;

@Service
public class MqService {

    private final JmsTemplate jmsTemplate;
    private final Queue queue;

    @Autowired
    public MqService(JmsTemplate jmsTemplate, Queue queue) {
        this.jmsTemplate = jmsTemplate;
        this.queue = queue;
    }

    public void connect() {
        // Example connection logic
        jmsTemplate.setReceiveTimeout(10000); // Just an example
        // Other connection-related code here
    }


    public void sendMessage(String message) {
        jmsTemplate.convertAndSend(queue, message);
    }
}
