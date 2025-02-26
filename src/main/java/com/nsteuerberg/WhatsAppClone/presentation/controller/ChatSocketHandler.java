package com.nsteuerberg.WhatsAppClone.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsteuerberg.WhatsAppClone.presentation.dto.request.ChatMessageRequest;
import com.nsteuerberg.WhatsAppClone.presentation.dto.response.ChatMessageResponse;
import com.nsteuerberg.WhatsAppClone.service.implementations.MessageServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(ChatSocketHandler.class);

    @Autowired
    private MessageServiceImpl messageService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            String name = session.getPrincipal().getName();
            messageService.userConnected(name, session);
        }catch (NullPointerException e){
            log.error("Usuario sin autenticar");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        try {
            messageService.userDisconnected(
                    session.getPrincipal().getName()
            );
        } catch (NullPointerException e) {
            log.error("El usuario no está autenticado");
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            messageService.messageReceived(
                    session.getPrincipal().getName(),
                    message
            );
        } catch (NullPointerException e) {
            log.error("El usuario no está autenticado");
        }
    }

}
