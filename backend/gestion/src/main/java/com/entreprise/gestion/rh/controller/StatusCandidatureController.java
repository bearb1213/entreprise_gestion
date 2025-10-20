package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.exception.MyException;
import com.entreprise.gestion.rh.model.StatusCandidature;
import com.entreprise.gestion.rh.service.StatusCandidatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/status-candidature")
@RequiredArgsConstructor
public class StatusCandidatureController {

    private final StatusCandidatureService statusCandidatureService;

    @PostMapping
    public ResponseEntity<?> createStatusCandidature(
            @RequestBody Map<String, Integer> request) {
        try {
            Integer candidatureId = request.get("candidatureId");
            Integer evaluationId = request.get("evaluationId");
            
            StatusCandidature status = statusCandidatureService.createStatusCandidature(candidatureId, evaluationId);
            return ResponseEntity.ok(status);
            
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/candidature/{candidatureId}")
    public ResponseEntity<?> getStatusByCandidature(@PathVariable Integer candidatureId) {
        try {
            List<StatusCandidature> statusList = statusCandidatureService.getStatusByCandidature(candidatureId);
            return ResponseEntity.ok(statusList);
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStatusById(@PathVariable Integer id) {
        try {
            StatusCandidature status = statusCandidatureService.getStatusById(id);
            return ResponseEntity.ok(status);
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStatusCandidature(@PathVariable Integer id) {
        try {
            statusCandidatureService.deleteStatusCandidature(id);
            return ResponseEntity.ok().build();
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}