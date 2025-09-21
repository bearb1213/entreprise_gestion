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

@RestController
@RequestMapping("/api/public/candidats")
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
    @PostMapping("/creer")
    public ResponseEntity<?> creerCandidat(
            @RequestBody Map<String, Object> candidateData,
            @RequestParam(value = "image_file", required = false) MultipartFile imageFile,
            @RequestParam(value = "cv_file", required = false) MultipartFile cvFile,
            @RequestParam(value = "diplome_files", required = false) MultipartFile[] diplomeFiles,
            @RequestParam(value = "autres_files", required = false) MultipartFile[] autresFiles
    ) {
        try {

            // Ajouter les fichiers dans la Map
            candidateData.put("image_file", imageFile);
            candidateData.put("cv_file", cvFile);
            candidateData.put("diplome_files", diplomeFiles != null ? java.util.Arrays.asList(diplomeFiles) : null);
            candidateData.put("autres_files", autresFiles != null ? java.util.Arrays.asList(autresFiles) : null);

            candidatService.creerCandidat(candidateData);

            return ResponseEntity.ok().body("Candidat créé avec succès");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur serveur : " + e.getMessage());
        }
    }
}
