package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class GroqChatService {

    private final WebClient webClient;

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    @Value("${groq.model}")
    private String model;

    // PLUS D’INJECTION PAR construct
    public GroqChatService() {
        this.webClient = WebClient.builder().build();
    }

    public String askHRBot(ChatRequest chatRequest) {
        String userContextText = "";
        if (chatRequest.getUserId() != null && !chatRequest.getUserId().isEmpty()) {
            userContextText = "L'utilisateur connecté a l'ID: " + chatRequest.getUserId() +
                    ". (Ici tu pourrais ajouter des infos comme le poste, le solde de congés, etc.)";
        }

        String policyContext = """
                Tu es un assistant RH pour une entreprise.
                Tu dois répondre uniquement en français.
                Règles :
                - Si tu ne connais pas une information précise, dis-le clairement.
                - Sois poli, clair et concis.
                - Si la question concerne des données personnelles d'un autre salarié que l'utilisateur, refuse de répondre (RGPD).
                - Pour les congés : 25 jours de congés payés par an (exemple).
                - Le télétravail : 3 jours par semaine pour les postes éligibles (exemple).
                """;

        String prompt = """
                CONTEXTE ENTREPRISE :
                %s

                CONTEXTE UTILISATEUR :
                %s

                QUESTION UTILISATEUR :
                %s

                INSTRUCTIONS :
                - Réponds uniquement en français.
                - Adapte ta réponse à ce contexte.
                - Ne révèle pas d'informations confidentielles.
                - Si c'est une procédure (ex: comment poser des congés), explique étape par étape.
                """.formatted(
                policyContext,
                userContextText.isBlank() ? "Aucune information personnalisée disponible." : userContextText,
                chatRequest.getMessage()
        );

        Map<String, Object> requestBody = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content", "Tu es un assistant RH expert de cette entreprise."),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.2
        );

        Map<String, Object> response = webClient.post()
                .uri(apiUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(Map.of());
                })
                .block();

        if (response == null || !response.containsKey("choices")) {
            return "Je n'ai pas pu générer de réponse pour le moment.";
        }

        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices.isEmpty()) {
                return "Je n'ai pas pu générer de réponse pour le moment.";
            }

            Map<String, Object> firstChoice = choices.get(0);
            Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
            String content = (String) message.get("content");
            return content != null ? content : "Je n'ai pas pu générer de réponse pour le moment.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors du traitement de la réponse du modèle.";
        }
    }
}