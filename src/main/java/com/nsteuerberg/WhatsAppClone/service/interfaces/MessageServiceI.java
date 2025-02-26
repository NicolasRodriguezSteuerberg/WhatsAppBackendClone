package com.nsteuerberg.WhatsAppClone.service.interfaces;

import com.nsteuerberg.WhatsAppClone.presentation.dto.request.ChatMessageRequest;
import com.nsteuerberg.WhatsAppClone.presentation.dto.response.ChatMessageResponse;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public interface MessageServiceI {
    void messageReceived(String username, TextMessage textMessage);
    void sendMessage(String receiver, ChatMessageResponse message);
    void sendMessageToGroup(String groupId, Object message);
    void userConnected(String name, WebSocketSession session);
    void userDisconnected(String name);
}
