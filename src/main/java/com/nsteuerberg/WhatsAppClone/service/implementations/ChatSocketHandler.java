package com.nsteuerberg.WhatsAppClone.service.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsteuerberg.WhatsAppClone.presentation.dto.request.ChatMessageRequest;
import com.nsteuerberg.WhatsAppClone.presentation.dto.response.ChatMessageResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionMap.put(session.getPrincipal().getName(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionMap.remove(session.getPrincipal().getName());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            ChatMessageRequest messageRequest = mapper.readValue(message.getPayload(), ChatMessageRequest.class);
            sessionMap.get(messageRequest.receiver())
                    .sendMessage(
                            new TextMessage(
                                    mapper.writeValueAsString(
                                            new ChatMessageResponse(
                                                    session.getPrincipal().getName(),
                                                    messageRequest.message()
                                            )
                                    )
                            )
                    );
        } catch (Exception e){
            session.sendMessage(new TextMessage(
                    mapper.writeValueAsString(
                        new ChatMessageResponse(
                                "SISTEMA",
                                "mensaje en formato incorrecto"
                        )
                    )
            ));
        }
    }

}
