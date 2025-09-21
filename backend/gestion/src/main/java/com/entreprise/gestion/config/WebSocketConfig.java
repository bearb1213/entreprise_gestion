package com.entreprise.gestion.rh.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Topic pour envoyer les messages aux clients
        config.enableSimpleBroker("/topic");
        // Préfixe pour envoyer des messages depuis le client vers le serveur
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Point de connexion WebSocket côté client
        registry.addEndpoint("/ws-notifications")
                .setAllowedOriginPatterns("*") // Autoriser toutes les origines pour test
                .withSockJS(); // fallback SockJS si WebSocket non supporté
    }
}
