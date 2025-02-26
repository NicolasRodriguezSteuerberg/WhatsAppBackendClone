package com.nsteuerberg.WhatsAppClone.configuration.app;

import com.nsteuerberg.WhatsAppClone.presentation.controller.ChatSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private ChatSocketHandler handler;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/chat");
                /*.addInterceptors(new HandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                        if (request instanceof ServletServerHttpRequest) {
                            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                            System.out.println(servletRequest.getHeaders());
                            Principal principal = servletRequest.getPrincipal();
                            if (principal != null) {
                                System.out.println("hola 3");
                                attributes.put("principal", principal);
                                return true;
                            }

                        }
                        response.setStatusCode(HttpStatus.UNAUTHORIZED);
                        return false;
                    }

                    @Override
                    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
                        // No-op
                    }
                });*/
    }
}
