package com.bankapp.messagerouter.service;

import com.bankapp.messagerouter.entity.Message;
import com.bankapp.messagerouter.entity.Partner;
import com.bankapp.messagerouter.repository.MessageRepository;
import com.bankapp.messagerouter.repository.PartnerRepository;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class MessageService {

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

//    @Value("${ibm.mq.queue}")
//    private String queue;


    @Value("${ibm.mq.inputQueue}")
    private String queue;

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    public Message sendMessage(String content, String senderId, String receiverId) {
        try {
            // Créer le message
            Message message = new Message();
            message.setContent(content);
            message.setSender(senderId);
            message.setReceiver(receiverId);
            message.setTimestamp(LocalDateTime.now());
            message.setProcessed(false);

            // Envoyer le message à la queue JMS
            jmsTemplate.convertAndSend(queue, content);

            // Sauvegarder dans la base de données
            return messageRepository.save(message);
        } catch (Exception e) {
            log.error("Failed to send and save message: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing the message");
        }
    }


    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }


    public Message getMessageById(Long id) {
        return messageRepository.findById(id).orElseThrow(() -> new RuntimeException("Message not found"));
    }

//    public Message saveMessage(Message message) {
//        return messageRepository.save(message);
//    }
    public Message saveMessage(Message message) {
        // Validation manuelle (si nécessaire)
        if (message.getContent() == null || message.getSender() == null || message.getReceiver() == null) {
            throw new IllegalArgumentException("Le contenu, l'expéditeur et le destinataire doivent être non null");
        }

        return messageRepository.save(message);
    }

    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }

    public Message editMessage(Long id, String newContent, String newSender, String newReceiver) {
        // Rechercher le message par ID
        Message existingMessage = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found with ID: " + id));

        // Mettre à jour les champs avec les nouvelles valeurs
        if (newContent != null) {
            existingMessage.setContent(newContent);
        }
        if (newSender != null) {
            existingMessage.setSender(newSender);
        }
        if (newReceiver != null) {
            existingMessage.setReceiver(newReceiver);
        }

        // Sauvegarder les modifications
        return messageRepository.save(existingMessage);
    }




    public Optional<Message> findById(Long id) {
        return messageRepository.findById(id);
    }
}
