package com.entreprise.gestion.rh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entreprise.gestion.rh.model.Notes;
import com.entreprise.gestion.rh.repository.NotesRepository;

@Service
public class NotesService {
    
    @Autowired
    private NotesRepository notesRepository;

    public Notes saveNotes(Notes note)
    {
        return notesRepository.save(note);
    }
}
