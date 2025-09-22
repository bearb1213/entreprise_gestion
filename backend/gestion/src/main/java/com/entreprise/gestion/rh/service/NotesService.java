package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.dto.NoteCreationDTO;
import com.entreprise.gestion.rh.model.Candidature;
import com.entreprise.gestion.rh.model.Evaluation;
import com.entreprise.gestion.rh.model.Notes;
import com.entreprise.gestion.rh.repository.CandidatureRepository;
import com.entreprise.gestion.rh.repository.EvaluationRepository;
import com.entreprise.gestion.rh.repository.NotesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotesService {
    private final NotesRepository notesRepository;
    private final CandidatureRepository candidatureRepository;
    private final EvaluationRepository evaluationRepository;

    public Notes saveNote(NoteCreationDTO noteDTO) {
        // Vérifier et récupérer la candidature
        Candidature candidature = candidatureRepository.findById(noteDTO.getCandidatureId())
                .orElseThrow(() -> new RuntimeException("Candidature non trouvée avec l'ID: " + noteDTO.getCandidatureId()));

        // Vérifier et récupérer l'évaluation
        Evaluation evaluation = evaluationRepository.findById(noteDTO.getEvaluationId())
                .orElseThrow(() -> new RuntimeException("Évaluation non trouvée avec l'ID: " + noteDTO.getEvaluationId()));

        // Vérifier si la note existe déjà pour cette candidature et évaluation
        if (notesRepository.existsByCandidatureIdAndEvaluationId(noteDTO.getCandidatureId(), noteDTO.getEvaluationId())) {
            throw new RuntimeException("Une note existe déjà pour cette candidature et cette évaluation");
        }

        // Valider la note
        if (noteDTO.getNote() == null || noteDTO.getNote() < 0 || noteDTO.getNote() > 20) {
            throw new RuntimeException("La note doit être comprise entre 0 et 20");
        }

        Notes note = new Notes();
        note.setNote(noteDTO.getNote());
        note.setEvaluation(evaluation);
        note.setCandidature(candidature);
        note.setDateEntree(LocalDateTime.now());

        return notesRepository.save(note);
    }

    public boolean existsByCandidatureAndEvaluation(Integer candidatureId, Integer evaluationId) {
        return notesRepository.existsByCandidatureIdAndEvaluationId(candidatureId, evaluationId);
    }
}