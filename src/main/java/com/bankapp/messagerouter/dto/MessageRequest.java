package com.bankapp.messagerouter.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class MessageRequest {

    private Long id;  // Add this field to the DTO

    @NotNull(message = "Content must not be null")
    @Size(min = 1, max = 500, message = "Content must be between 1 and 500 characters")
    private String content;

    @NotNull(message = "Sender must not be null")
    private String sender;

    @NotNull(message = "Receiver must not be null")
    private String receiver;

    @NotNull(message = "Timestamp must not be null")
    private LocalDateTime timestamp; // Assuming you want to use LocalDateTime for the timestamp field

    private Boolean processed; // Assuming 'processed' is a boolean flag

    // Getters and Setters
}
