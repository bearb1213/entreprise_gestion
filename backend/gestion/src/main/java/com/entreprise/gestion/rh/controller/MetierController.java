package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.rh.model.Metier;
import com.entreprise.gestion.rh.service.MetierService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/metiers")
@RequiredArgsConstructor
public class MetierController {

    private final MetierService metierService;

    // GET /api/metiers -> tous les métiers
    @GetMapping
    public List<Metier> getAllMetiers() {
        return metierService.getAll();
    }

    // GET /api/metiers/{id} -> métier par ID
    @GetMapping("/{id}")
    public Metier getMetierById(@PathVariable Integer id) {
        return metierService.getById(id);
    }
}
