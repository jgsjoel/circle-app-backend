package com.chat.messages.messages.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncomingMessageDto {

    @NotEmpty(message = "Receiver Mobile No Required")
    private String receiverMobile;
    @NotEmpty(message = "Sender Id Required")
    private String senderId;
    @NotEmpty(message = "Message Is Required")
    private String message;
    private List<MediaDto> mediaDtos;

    @AssertTrue(message = "Either message or mediaUris must be provided")
    public boolean isMessageOrMediaPresent() {
        boolean hasMessage = message != null && !message.isBlank();
        boolean hasMedia = mediaDtos != null && !mediaDtos.isEmpty();
        return hasMessage || hasMedia;
    }

}
