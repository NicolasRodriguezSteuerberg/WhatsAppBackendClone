package com.nsteuerberg.WhatsAppClone.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChatMessageRequest(
        @NotBlank
        String receiver,
        @NotBlank
        String message
) {
}
