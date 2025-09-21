package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.rh.model.Personne;
import com.entreprise.gestion.rh.service.PersonneService;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personnes")
public class PersonneController {

    private final PersonneService personneService;

    public PersonneController(PersonneService personneService) {
        this.personneService = personneService;
    }

    // ✅ Insérer une personne
    @PreAuthorize("hasRole('isAuthenticated'")
    @PostMapping
    public Personne create(@RequestBody Personne personne) {
        return personneService.createPersonne(personne);
    }

    // ✅ Récupérer toutes les personnes
    @PreAuthorize("hasRole('isAuthenticated'")
    @GetMapping
    public List<Personne> getAll() {
        return personneService.getAllPersonnes();
    }
}
