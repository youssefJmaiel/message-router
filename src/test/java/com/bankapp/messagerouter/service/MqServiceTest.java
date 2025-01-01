package com.bankapp.messagerouter.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;

import javax.jms.Queue;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MqServiceTest {

    @Mock
    private JmsTemplate jmsTemplate;

    @Mock
    private Queue queue;

    @InjectMocks
    private MqService mqService;

    @Test
    void testConnect() {
        // Call the method under test
        mqService.connect();

        // Verify that certain methods were called on jmsTemplate (example: setReceiveTimeout)
        verify(jmsTemplate).setReceiveTimeout(anyLong()); // Check if this method is called
    }



    @Test
    void testSendMessage() {
        // Ensure that the mock is properly injected
        assertNotNull(mqService, "MqService should not be null");
        assertNotNull(jmsTemplate, "jmsTemplate should not be null");
        assertNotNull(queue, "queue should not be null");

        // Call the method under test
        mqService.sendMessage("Test message");

        // Verify that convertAndSend was called on jmsTemplate with the correct parameters
        verify(jmsTemplate).convertAndSend(eq(queue), eq("Test message"));
    }





}
