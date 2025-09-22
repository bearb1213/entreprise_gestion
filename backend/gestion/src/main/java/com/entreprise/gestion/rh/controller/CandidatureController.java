package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.rh.dto.CandidatureSimpleDTO;
import com.entreprise.gestion.rh.service.CandidatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidatures")
@RequiredArgsConstructor
public class CandidatureController {

    private final CandidatureService candidatureService;

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

    @GetMapping("/candidat/{candidatId}")
    public ResponseEntity<List<CandidatureSimpleDTO>> getCandidaturesByCandidat(@PathVariable Integer candidatId) {
        return ResponseEntity.ok(candidatureService.getCandidaturesByCandidat(candidatId));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<CandidatureSimpleDTO> updateStatutCandidature(
            @PathVariable Integer id, 
            @RequestParam Integer statut) {
        return ResponseEntity.ok(candidatureService.updateStatutCandidature(id, statut));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidature(@PathVariable Integer id) {
        candidatureService.deleteCandidature(id);
        return ResponseEntity.noContent().build();
    }

    // NOTE: Les endpoints POST et PUT sont supprimés car
    // ils nécessitent un DTO complet avec les objets Besoin et Candidat
    // Vous devriez créer un contrôleur séparé avec un DTO complet pour ces opérations
}
