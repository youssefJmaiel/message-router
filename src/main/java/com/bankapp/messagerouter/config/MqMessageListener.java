package com.bankapp.messagerouter.config;

import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;

@Component
public class MqMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        // Traitement du message reçu
        try {
            String text = message.getBody(String.class); // Récupérer le message sous forme de String
            System.out.println("Received: " + text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
