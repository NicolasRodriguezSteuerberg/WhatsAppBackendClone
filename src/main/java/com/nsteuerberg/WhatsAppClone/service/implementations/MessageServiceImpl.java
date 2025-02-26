package com.nsteuerberg.WhatsAppClone.service.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsteuerberg.WhatsAppClone.presentation.dto.request.ChatMessageRequest;
import com.nsteuerberg.WhatsAppClone.presentation.dto.response.ChatMessageResponse;
import com.nsteuerberg.WhatsAppClone.service.interfaces.MessageServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageServiceImpl implements MessageServiceI {

    private final Map<String, WebSocketSession> sockets = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Override
    public void userConnected(String name, WebSocketSession session) {
        sockets.put(name, session);
    }

    @Override
    public void userDisconnected(String name) {
        try {
            sockets.remove(name);
        } catch (Exception e) {
            log.error("El usuario no se pudo desconectar");
        }
    }

    @Override
    public void messageReceived(String sender, TextMessage textMessage){
        /*
         * 4 Tipos de mensajes
         * - Mensaje a enviar
         * - Modificación entregado/recibido/visto de mensaje
         * - Mensaje de grupo
         */
        try {
            ChatMessageRequest messageRequest = mapper.readValue(textMessage.getPayload(), ChatMessageRequest.class);
            sendMessage(
                    messageRequest.receiver(),
                    new ChatMessageResponse(
                        sender, messageRequest.message()
                    )
            );
        } catch (JsonProcessingException e) {
            log.error("No se pudo convertir el mensaje: " + e.getMessage());
            sendSistemMessage(sender, "Mensaje en formato erróneo");
        }
    }

    public void sendSistemMessage(String receiver, String message){
        try {
            sockets.get(receiver).sendMessage(new TextMessage(
                    mapper.writeValueAsString(new ChatMessageResponse(
                            "SISTEMA",
                            message
                    ))
            ));
        } catch (Exception ex){
            log.error("No se pudo enviar el mensaje de no enviado " + ex.getMessage());
        }
    }

    @Override
    public void sendMessage(String receiver, ChatMessageResponse message) {
        try {
            TextMessage textMessage = new TextMessage(mapper.writeValueAsString(message));
            sendSistemMessage(message.sender(), "Mensaje recibido por el sistema");
            WebSocketSession socket = sockets.get(receiver);

            if (socket != null){
                socket.sendMessage(textMessage);
            } else{
                // ToDo save it in database
                sendSistemMessage(message.sender(), "El usuario no está conectado");
            }
        } catch (JsonProcessingException e) {
            log.error("NO se pudo enviar el mensaje al destinatario: " + e.getMessage());
            sendSistemMessage(message.sender(), "El mensaje está en formato erróneo");
        } catch (IOException e) {
            log.error("NO se pudo enviar el mensaje al destinatario: " + e.getMessage());
            sendSistemMessage(message.sender(), "El mensaje no se pudo enviar");
        }
    }

    @Override
    public void sendMessageToGroup(String groupId, Object message) {

    }
}
