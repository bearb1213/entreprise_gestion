package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.exception.MyException;
import com.entreprise.gestion.rh.model.Candidat;
import com.entreprise.gestion.rh.repository.CandidatRepository;
import com.entreprise.gestion.rh.service.CandidatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/api/public/candidature")
@RequiredArgsConstructor
public class CandidatureController {

    private final CandidatureService candidatureService;

    @PreAuthorize("hasRole('CANDIDAT')")
    @PostMapping("/candidater")
    public ResponseEntity<?> candidater(
            @RequestParam Integer idBesoin,
            HttpSession session
    ) {
        try {
            // 🔹 Récupérer l'email du candidat depuis la session
            Map<String, Object> authInfo = (Map<String, Object>) session.getAttribute("auth");
            if (authInfo == null || !authInfo.containsKey("email")) {
                throw new MyException("Email du candidat introuvable dans la session");
            }

            String email = (String) authInfo.get("email");

            // 🔹 Appeler le service
            candidatureService.candidater(email, idBesoin);

            return ResponseEntity.ok(Map.of(
                    "message", "Candidature enregistrée avec succès",
                    "emailCandidat", email,
                    "idBesoin", idBesoin
            ));

        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Erreur inattendue : " + e.getMessage()
            ));
        }
    }
}
