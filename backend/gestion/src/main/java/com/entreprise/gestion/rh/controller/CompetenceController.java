package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.rh.model.Competence;
import com.entreprise.gestion.rh.service.CompetenceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class CompetenceController {

    private final CompetenceService competenceService;

    public CompetenceController(CompetenceService competenceService) {
        this.competenceService = competenceService;
    }

    @GetMapping("/api/competences")
    // GET /api/competences -> renvoie la liste de toutes les compétences
    public List<Competence> getAllCompetences() {
        return competenceService.getAll();
    }
}
