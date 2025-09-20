package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.rh.model.Personne;
import com.entreprise.gestion.rh.service.PersonneService;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personnes")
public class PersonneController {

    private final PersonneService personneService;

    public PersonneController(PersonneService personneService) {
        this.personneService = personneService;
    }

    // ✅ Insérer une personne
    @PostMapping
    public Personne create(@RequestBody Personne personne) {
        return personneService.createPersonne(personne);
    }

    // ✅ Récupérer toutes les personnes
    @GetMapping
    public List<Personne> getAll() {
        return personneService.getAllPersonnes();
    }
}
