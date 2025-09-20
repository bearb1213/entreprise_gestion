package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.rh.model.Competence;
import com.entreprise.gestion.rh.service.CompetenceService;
import com.entreprise.gestion.rh.dto.CompetenceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/competences")
public class CompetenceController {

    @Autowired
    private CompetenceService competenceService;

    @GetMapping
    public ResponseEntity<List<CompetenceDTO>> getAllCompetences() {
        List<CompetenceDTO> competences = competenceService.getAll().stream()
                .map(comp -> new CompetenceDTO(comp.getId(), comp.getLibelle()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(competences);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetenceDTO> getCompetenceById(@PathVariable Integer id) {
        Competence competence = competenceService.getById(id);
        if (competence != null) {
            CompetenceDTO dto = new CompetenceDTO(competence.getId(), competence.getLibelle());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<CompetenceDTO> createCompetence(@RequestBody Competence competence) {
        Competence createdCompetence = competenceService.createCompetence(competence);
        CompetenceDTO dto = new CompetenceDTO(createdCompetence.getId(), createdCompetence.getLibelle());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetenceDTO> updateCompetence(@PathVariable Integer id, @RequestBody Competence competence) {
        Competence updatedCompetence = competenceService.updateCompetence(id, competence);
        CompetenceDTO dto = new CompetenceDTO(updatedCompetence.getId(), updatedCompetence.getLibelle());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompetence(@PathVariable Integer id) {
        competenceService.deleteCompetence(id);
        return ResponseEntity.noContent().build();
    }
}