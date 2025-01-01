package com.bankapp.messagerouter.controller;

import com.bankapp.messagerouter.entity.Message;
import com.bankapp.messagerouter.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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

}
