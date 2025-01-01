package com.bankapp.messagerouter.service;

import com.bankapp.messagerouter.entity.Message;
import com.bankapp.messagerouter.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
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



    public Optional<Message> findById(Long id) {
        return messageRepository.findById(id);
    }
}
