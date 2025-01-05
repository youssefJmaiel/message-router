package com.bankapp.messagerouter.controller;

import com.bankapp.messagerouter.dto.MessageRequest;
import com.bankapp.messagerouter.entity.Message;
import com.bankapp.messagerouter.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @InjectMocks
    private MessageController messageController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    public MessageControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMessages() throws Exception {
        // Simule une réponse du service
        Message message = new Message();
        message.setContent("Hello, World!");
        message.setSender("Alice");
        message.setReceiver("Bob");

        when(messageService.getAllMessages()).thenReturn(List.of(message));

        // Envoie une requête GET et vérifie la réponse
        mockMvc.perform(get("/api/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Hello, World!"))
                .andExpect(jsonPath("$[0].sender").value("Alice"))
                .andExpect(jsonPath("$[0].receiver").value("Bob"));
    }

    @Test
    void testSaveMessage() throws Exception {
        // Crée un message à enregistrer
        Message message = new Message();
        message.setContent("New Message");
        message.setSender("Alice");
        message.setReceiver("Bob");

        // Convertit l'objet message en JSON
        String messageJson = new ObjectMapper().writeValueAsString(message);

        // Simule une réponse du service
        when(messageService.saveMessage(any(Message.class))).thenReturn(message);

        // Envoie une requête POST pour enregistrer le message et vérifie la réponse
        mockMvc.perform(post("/api/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(messageJson))
                .andExpect(status().isCreated()) // 201 Created
                .andExpect(jsonPath("$.content").value("New Message"))
                .andExpect(jsonPath("$.sender").value("Alice"))
                .andExpect(jsonPath("$.receiver").value("Bob"));
    }

    @Test
    public void deleteMessage_shouldReturnNoContent_whenSuccessful() throws Exception {
        // Setup: mock the service method
        Long messageId = 1L;
        when(messageService.findById(messageId)).thenReturn(Optional.of(new Message())); // Simuler l'existence du message

        mockMvc.perform(delete("/api/message/{id}", messageId))
                .andExpect(status().isNoContent()); // Attendre 204 No Content
    }

    @Test
    public void deleteMessage_shouldReturnNotFound_whenMessageNotFound() throws Exception {
        // Setup: mock the service method to return Optional.empty (message non trouvé)
        Long messageId = 1L;
        when(messageService.findById(messageId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/message/{id}", messageId))
                .andExpect(status().isNotFound()); // Attendre 404 Not Found
    }



    @Test
    void testEditMessage() throws Exception {
        Long messageId = 1L;
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setContent("Updated content");
        messageRequest.setSender("sender@example.com");
        messageRequest.setReceiver("receiver@example.com");
        messageRequest.setTimestamp(LocalDateTime.now());
        messageRequest.setProcessed(false);

        // Créez un objet Message simulé comme réponse du service
        Message updatedMessage = new Message();
        updatedMessage.setId(messageId);
        updatedMessage.setContent(messageRequest.getContent());
        updatedMessage.setSender(messageRequest.getSender());
        updatedMessage.setReceiver(messageRequest.getReceiver());
        updatedMessage.setTimestamp(messageRequest.getTimestamp());
        updatedMessage.setProcessed(messageRequest.getProcessed());

        // Simulez le comportement du service
        when(messageService.editMessage(eq(messageId), eq(messageRequest.getContent()), eq(messageRequest.getSender()), eq(messageRequest.getReceiver()), eq(messageRequest.getTimestamp()), eq(messageRequest.getProcessed())))
                .thenReturn(updatedMessage);

        // Configure ObjectMapper with JavaTimeModule to handle LocalDateTime
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Effectuez la requête PUT et vérifiez la réponse
        mockMvc.perform(put("/api/message/{id}", messageId)  // Assurez-vous d'inclure "/api" si nécessaire
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(messageId))
                .andExpect(jsonPath("$.content").value(messageRequest.getContent()))
                .andExpect(jsonPath("$.sender").value(messageRequest.getSender()))
                .andExpect(jsonPath("$.receiver").value(messageRequest.getReceiver()))
                .andExpect(jsonPath("$.timestamp").exists())  // Ensure timestamp is included in the response
                .andExpect(jsonPath("$.processed").value(messageRequest.getProcessed()));
    }





}
