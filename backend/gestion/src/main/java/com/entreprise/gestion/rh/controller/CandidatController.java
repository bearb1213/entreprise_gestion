package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.rh.model.Candidat;
import com.entreprise.gestion.rh.service.CandidatService;
import com.entreprise.gestion.exception.MyException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/candidats")
@RequiredArgsConstructor
public class CandidatController {

    private final CandidatService candidatService;

    /**
     * Création d'un candidat avec ses compétences, langues, diplômes et fichiers
     * @param data JSON en string contenant les informations du candidat
     * @param imageFile Image du candidat
     * @param cvFile CV du candidat
     * @param diplomeFiles Liste des fichiers diplômes
     * @param autresFiles Autres fichiers
     * @return le candidat créé
     */
    @PostMapping(value="/creer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> creerCandidat(
            @RequestParam("data") String candidatDataJson,  // Toutes les données en JSON
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> candidateData = mapper.readValue(candidatDataJson, Map.class);
            
            // Traitement...
            candidatService.creerCandidat(candidateData);
            
            if (imageFile != null && !imageFile.isEmpty()) {
                candidatService.saveFile(imageFile, "images/");
            }
            
            return ResponseEntity.ok("Candidat créé avec succès");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value="/sary" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile imageFile) {
        try {
            candidatService.saveFile(imageFile , "images/");
            return ResponseEntity.ok("Image enregistrée avec succès");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCandidats() {
        try {
            Map<String, Object> result = candidatService.listerCandidats();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Erreur lors de la récupération de la liste des candidats");
            error.put("error", e.getMessage());
            error.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    // GET BY ID - Retourne un candidat spécifique par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCandidatById(@PathVariable Integer id) {
        try {
            Map<String, Object> result = candidatService.getCandidatMap(id);
            return ResponseEntity.ok(result);
        } catch (MyException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            error.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Erreur lors de la récupération du candidat");
            error.put("error", e.getMessage());
            error.put("timestamp", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
