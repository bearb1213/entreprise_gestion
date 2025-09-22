package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.rh.dto.CandidatureDTO;
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

    @GetMapping
    public ResponseEntity<List<CandidatureDTO>> getAllCandidatures() {
        return ResponseEntity.ok(candidatureService.getAllCandidatures());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidatureDTO> getCandidatureById(@PathVariable Integer id) {
        return ResponseEntity.ok(candidatureService.getCandidatureById(id));
    }

    @PostMapping
    public ResponseEntity<CandidatureDTO> createCandidature(@RequestBody CandidatureDTO candidatureDTO) {
        return ResponseEntity.ok(candidatureService.createCandidature(candidatureDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidatureDTO> updateCandidature(@PathVariable Integer id, @RequestBody CandidatureDTO candidatureDTO) {
        return ResponseEntity.ok(candidatureService.updateCandidature(id, candidatureDTO));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<CandidatureDTO> updateStatutCandidature(@PathVariable Integer id, @RequestParam Integer statut) {
        return ResponseEntity.ok(candidatureService.updateStatutCandidature(id, statut));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidature(@PathVariable Integer id) {
        candidatureService.deleteCandidature(id);
        return ResponseEntity.noContent().build();
    }
}