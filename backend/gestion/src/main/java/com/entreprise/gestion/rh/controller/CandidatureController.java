package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.exception.MyException;
import com.entreprise.gestion.rh.model.Candidat;
import com.entreprise.gestion.rh.dto.CandidatureSimpleDTO;
import com.entreprise.gestion.rh.model.Candidature;
import com.entreprise.gestion.rh.repository.CandidatRepository;
import com.entreprise.gestion.rh.service.CandidatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.HashMap;


@RestController
@RequestMapping("/api/candidature")
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

    @GetMapping({"/infos/{id}","/infos/{id}/"})
    public Map<String,Object> getInfos(@PathVariable("id") Integer id) {
        Map<String,Object> infos = new HashMap<>();
        try {
            Candidature candidature = candidatureService.findCandidatureById(id);
            infos.put("id_metier",candidature.getBesoin().getMetier().getId());
            infos.put("id_dept",candidature.getBesoin().getDepartement().getId());
        } catch (Exception e) {
            infos.put("error", "Candidature introuvable");
            e.printStackTrace();
        }
        return infos;
    }
  
    @GetMapping
    public ResponseEntity<List<CandidatureSimpleDTO>> getAllCandidatures() {
        return ResponseEntity.ok(candidatureService.getAllCandidatures());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidatureSimpleDTO> getCandidatureById(@PathVariable Integer id) {
        return ResponseEntity.ok(candidatureService.getCandidatureById(id));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<CandidatureSimpleDTO>> getCandidaturesByStatut(@PathVariable Integer statut) {
        return ResponseEntity.ok(candidatureService.getCandidaturesByStatut(statut));
    }

    @GetMapping("/besoin/{besoinId}")
    public ResponseEntity<List<CandidatureSimpleDTO>> getCandidaturesByBesoin(@PathVariable Integer besoinId) {
        return ResponseEntity.ok(candidatureService.getCandidaturesByBesoin(besoinId));
    }

    @GetMapping("/candidature/{candidatId}")
    public ResponseEntity<List<CandidatureSimpleDTO>> getCandidaturesByCandidat(@PathVariable Integer candidatId) {
        return ResponseEntity.ok(candidatureService.getCandidaturesByCandidat(candidatId));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getCandidatureDetails(@PathVariable Integer id) {
        try {
            Map<String, Object> details = candidatureService.getCandidatureDetails(id);
            return ResponseEntity.ok(details);
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

    @PreAuthorize("hasRole('RH') or hasRole('UNITE')")
    @PostMapping("/{id}/statut")
    public ResponseEntity<?> updateStatutCandidature(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> request) {
        try {
            Integer nouveauStatut = (Integer) request.get("statut");
            
            if (nouveauStatut == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Le champ 'statut' est obligatoire"
                ));
            }
            
            // Validation du statut
            if (nouveauStatut < 0 || nouveauStatut > 3) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Statut invalide. Valeurs acceptées: 0=En attente, 1=En étude, 2=Acceptée, 3=Refusée"
                ));
            }
            
            candidatureService.updateStatutCandidature(id, nouveauStatut);
            
            return ResponseEntity.ok(Map.of(
                "message", "Statut de la candidature mis à jour avec succès",
                "candidatureId", id,
                "nouveauStatut", nouveauStatut,
                "statutLibelle", getStatutLibelle(nouveauStatut)
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

    private String getStatutLibelle(Integer statut) {
        switch (statut) {
            case 0: return "En attente";
            case 1: return "En étude";
            case 2: return "Acceptée";
            case 3: return "Refusée";
            default: return "Inconnu";
        }
    }

}
