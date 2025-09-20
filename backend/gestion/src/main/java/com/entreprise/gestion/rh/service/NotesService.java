package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.model.Candidature;
import com.entreprise.gestion.rh.model.Evaluation;
import com.entreprise.gestion.rh.model.Notes;
import com.entreprise.gestion.rh.repository.NotesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotesService {
    private final NotesRepository notesRepository;

    public Notes saveNote(Double score, Evaluation evaluation, Candidature candidature) {
        Notes note = new Notes();
        note.setNote(score);
        note.setEvaluation(evaluation);
        note.setCandidature(candidature);
        note.setDateEntree(LocalDateTime.now());
        return notesRepository.save(note);
    }
}
