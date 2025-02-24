package com.nsteuerberg.WhatsAppClone.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"sender", "message"})
public record ChatMessageResponse(
        String sender,
        String message
) {
}
