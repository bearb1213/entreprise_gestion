package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.rh.model.Langue;
import com.entreprise.gestion.rh.service.LangueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/langues")
@RequiredArgsConstructor
public class LangueController {

    private final LangueService langueService;

    // GET /api/langues -> toutes les langues
    @GetMapping
    public List<Langue> getAllLangues() {
        return langueService.getAll();
    }

    // GET /api/langues/{id} -> langue par ID
    @GetMapping("/{id}")
    public Langue getLangueById(@PathVariable Integer id) {
        return langueService.getById(id);
    }
}
