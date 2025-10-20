package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.rh.service.EntretienService;
import com.entreprise.gestion.rh.model.Entretien;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@RestController
@RequestMapping("/api/entretiens")
@RequiredArgsConstructor
public class EntretienController {
    
    private final EntretienService entretienService;

    /**
     * Récupère tous les entretiens futurs d'un RH
     */
    @GetMapping("/rh/{rhId}/futurs")
    public ResponseEntity<?> getEntretiensFuturs(@PathVariable Integer rhId) {
        try {
            List<Map<String, Object>> entretiens = entretienService.getEntretiensFutursByRhId(rhId);
            return ResponseEntity.ok(Map.of(
                "entretiens", entretiens,
                "total", entretiens.size(),
                "rhId", rhId
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage()
            ));
        }
    }

    // Créez une classe DTO
    @Data
    public static class EntretienRequestDTO {
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime dateHeureDebut;
        
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime dateHeureFin;
        
        private Integer rhId;
        private Integer candidatureId;
    }

    // Modifiez le contrôleur
    @PostMapping
    public ResponseEntity<?> createEntretien(@RequestBody EntretienRequestDTO request) {
        try {
            System.out.println("=== DONNÉES REÇUES ===");
            System.out.println("Request: " + request);

            Entretien savedEntretien = entretienService.createEntretien(
                request.getRhId(),
                request.getCandidatureId(),
                request.getDateHeureDebut(),
                request.getDateHeureFin()
            );

            return ResponseEntity.ok(Map.of(
                "message", "Entretien créé avec succès",
                "entretienId", savedEntretien.getId()
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}