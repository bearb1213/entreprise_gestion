package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.rh.dto.CandidatDTO;
import com.entreprise.gestion.rh.dto.EvaluationResultDTO;
import com.entreprise.gestion.rh.model.Besoin;
import com.entreprise.gestion.rh.model.Candidat;
import com.entreprise.gestion.rh.service.CandidatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.entreprise.gestion.rh.model.Personne;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/candidat")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class CandidatController {
    
    private final CandidatService candidatService;
    
    @PostMapping
    public ResponseEntity<CandidatDTO> createCandidat(@RequestBody CandidatDTO candidatDTO) {
        Candidat candidat = convertToEntity(candidatDTO);
        Candidat savedCandidat = candidatService.saveCandidat(candidat);
        return ResponseEntity.ok(convertToDTO(savedCandidat));
    }
    
    @PostMapping("/evaluate")
    public ResponseEntity<EvaluationResultDTO> evaluateCandidat(@RequestBody CandidatDTO candidatDTO) {
        Candidat candidat = convertToEntity(candidatDTO);
        
        EvaluationResultDTO result = new EvaluationResultDTO();
        result.setCandidat(candidatDTO);
        
        List<Besoin> besoins = candidatService.getActiveBesoins();
        Map<String, Integer> scores = new HashMap<>();
        
        for (Besoin besoin : besoins) {
            int score = candidatService.calculateScore(candidat, besoin);
            scores.put(besoin.getMetier().getLibelle() + " - " + besoin.getDepartement().getLibelle(), score);
        }
        
        result.setScores(scores);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/besoins")
    public ResponseEntity<List<Besoin>> getActiveBesoins() {
        List<Besoin> besoins = candidatService.getActiveBesoins();
        return ResponseEntity.ok(besoins);
    }
    
    private Candidat convertToEntity(CandidatDTO dto) {
        Candidat candidat = new Candidat();
        candidat.setId(dto.getId());
        candidat.setDescription(dto.getDescription());
        
        // Convertir PersonneDTO to Personne entity
        if (dto.getPersonne() != null) {
            Personne personne = new Personne();
            personne.setId(dto.getPersonne().getId());
            personne.setNom(dto.getPersonne().getNom());
            personne.setPrenom(dto.getPersonne().getPrenom());
            personne.setEmail(dto.getPersonne().getEmail());
            personne.setDateNaissance(dto.getPersonne().getDateNaissance());
            personne.setGenre(dto.getPersonne().getGenre());
            personne.setVille(dto.getPersonne().getVille());
            personne.setTelephone(dto.getPersonne().getTelephone());
            candidat.setPersonne(personne);
        }
        
        // Les autres conversions (compétences, langues, etc.) seraient implémentées ici
        // Pour simplifier, nous les omettons dans cet exemple
        
        return candidat;
    }
    
    private CandidatDTO convertToDTO(Candidat candidat) {
        CandidatDTO dto = new CandidatDTO();
        dto.setId(candidat.getId());
        dto.setDescription(candidat.getDescription());
        
        if (candidat.getPersonne() != null) {
            CandidatDTO.PersonneDTO personneDTO = new CandidatDTO.PersonneDTO();
            personneDTO.setId(candidat.getPersonne().getId());
            personneDTO.setNom(candidat.getPersonne().getNom());
            personneDTO.setPrenom(candidat.getPersonne().getPrenom());
            personneDTO.setEmail(candidat.getPersonne().getEmail());
            personneDTO.setDateNaissance(candidat.getPersonne().getDateNaissance());
            personneDTO.setGenre(candidat.getPersonne().getGenre());
            personneDTO.setVille(candidat.getPersonne().getVille());
            personneDTO.setTelephone(candidat.getPersonne().getTelephone());
            dto.setPersonne(personneDTO);
        }
        
        return dto;
    }
}