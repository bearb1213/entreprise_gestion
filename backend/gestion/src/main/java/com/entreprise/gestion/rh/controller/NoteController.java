package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.rh.dto.NoteCreationDTO;
import com.entreprise.gestion.rh.model.Notes;
import com.entreprise.gestion.rh.service.NotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NotesService notesService;

    @PostMapping
    public ResponseEntity<?> createNote(@RequestBody NoteCreationDTO noteDTO) {
        try {
            Notes savedNote = notesService.saveNote(noteDTO);
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Note ajoutée avec succès",
                "noteId", savedNote.getId()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "status", "error", 
                "message", "Erreur lors de la création de la note"
            ));
        }
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkNoteExists(
            @RequestParam Integer candidatureId,
            @RequestParam Integer evaluationId) {
        boolean exists = notesService.existsByCandidatureAndEvaluation(candidatureId, evaluationId);
        return ResponseEntity.ok(exists);
    }
}