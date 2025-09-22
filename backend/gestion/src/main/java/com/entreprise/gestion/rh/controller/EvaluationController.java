package com.entreprise.gestion.rh.controller;


import com.entreprise.gestion.rh.dto.EvaluationDTO;
import com.entreprise.gestion.rh.service.EvaluationService;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService EvaluationService;

    @GetMapping
    public ResponseEntity<List<EvaluationDTO>> getAllEvaluations() {
        return ResponseEntity.ok(EvaluationService.getAllEvaluations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationDTO> getEvaluationById(@PathVariable Integer id) {
        return ResponseEntity.ok(EvaluationService.getEvaluationById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<EvaluationDTO>> getEvaluationsByLibelle(@RequestParam String libelle) {
        return ResponseEntity.ok(EvaluationService.getEvaluationsByLibelle(libelle));
    }

    @GetMapping("/coeff-min/{minCoeff}")
    public ResponseEntity<List<EvaluationDTO>> getEvaluationsByCoeffMin(@PathVariable Integer minCoeff) {
        return ResponseEntity.ok(EvaluationService.getEvaluationsByCoeffMin(minCoeff));
    }

    @PostMapping
    public ResponseEntity<EvaluationDTO> createEvaluation(@RequestBody EvaluationDTO evaluationDTO) {
        return ResponseEntity.ok(EvaluationService.createEvaluation(evaluationDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EvaluationDTO> updateEvaluation(@PathVariable Integer id, @RequestBody EvaluationDTO evaluationDTO) {
        return ResponseEntity.ok(EvaluationService.updateEvaluation(id, evaluationDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Integer id) {
        EvaluationService.deleteEvaluation(id);
        return ResponseEntity.noContent().build();
    }
} 
    
