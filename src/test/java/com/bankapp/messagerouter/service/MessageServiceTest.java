package com.bankapp.messagerouter.service;

import com.bankapp.messagerouter.entity.Message;
import com.bankapp.messagerouter.repository.MessageRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;  // Utilise any() de Mockito


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        // Simule un message retourné par le repository
        Message message = new Message();
        message.setContent("Hello, World!");
        message.setSender("Alice");
        message.setReceiver("Bob");
        message.setTimestamp(LocalDateTime.now());
        message.setProcessed(false);


        // Configure le comportement du mock
        when(messageRepository.findAll()).thenReturn(List.of(message));

//        lenient().when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> {
//            Message savedMessage = invocation.getArgument(0);
//            savedMessage.setId(1L); // Simule l'assignation de l'ID après la sauvegarde
//            return savedMessage;
//        });

    }

@Test
void testSaveMessage() {
    Message message = new Message();
    message.setContent("Test Content");
    message.setSender("Alice");
    message.setReceiver("Bob");

    // Mock le comportement du repository
    when(messageRepository.save(any(Message.class))).thenReturn(message);

    // Appel de la méthode à tester
    Message savedMessage = messageService.saveMessage(message);

    // Vérifications
    assertNotNull(savedMessage);
    assertEquals("Test Content", savedMessage.getContent());
    assertEquals("Alice", savedMessage.getSender());
    assertEquals("Bob", savedMessage.getReceiver());

    // Vérifie que save a bien été appelé
    verify(messageRepository, times(1)).save(message);
}

    @Test
    void testGetAllMessages() {
        List<Message> messages = messageService.getAllMessages();

        assertNotNull(messages);
        System.out.println(messages);// Vérifie que la liste n'est pas null
        Assert.assertFalse(messages.isEmpty()); // Vérifie que la liste n'est pas vide
        assertEquals(1, messages.size()); // Vérifie que la liste contient un message
        assertEquals("Hello, World!", messages.get(0).getContent()); // Vérifie le contenu
    }
    @Test
    void testGetAllMessagesEmpty() {
        when(messageRepository.findAll()).thenReturn(Collections.emptyList());
        List<Message> messages = messageService.getAllMessages();

        assertNotNull(messages);
        assertTrue(messages.isEmpty()); // Vérifie que la liste est vide
    }
    @Test
    void testGetAllMessagesWithException() {
        when(messageRepository.findAll()).thenThrow(new RuntimeException("Database error"));
        assertThrows(RuntimeException.class, () -> messageService.getAllMessages());
    }
    @Test
    void testSaveMessageThrowsException() {
        Message message = new Message();
        when(messageRepository.save(any(Message.class))).thenThrow(new RuntimeException("Error saving message"));

        assertThrows(RuntimeException.class, () -> messageService.saveMessage(message));
    }

    @Test
    void deleteMessage_shouldCallRepository() {
        // Arrange
        Long messageId = 1L;

        // Act
        messageService.deleteMessage(messageId);

        // Assert
        verify(messageRepository, times(1)).deleteById(messageId);
    }


    @Test
    void getMessageById_shouldReturnMessage_whenFound() {
        // Arrange
        Long messageId = 1L;
        Message message = new Message();
        message.setId(messageId);
        message.setContent("Test Message");
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        // Act
        Message foundMessage = messageService.getMessageById(messageId);

        // Assert
        assertNotNull(foundMessage);
        assertEquals("Test Message", foundMessage.getContent());
        verify(messageRepository, times(1)).findById(messageId);
    }

    @Test
    void getMessageById_shouldThrowException_whenNotFound() {
        // Arrange
        Long messageId = 1L;
        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> messageService.getMessageById(messageId));
        verify(messageRepository, times(1)).findById(messageId);
    }







}

