package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.rh.model.Departement;
import com.entreprise.gestion.rh.service.DepartementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departements")
@RequiredArgsConstructor
public class DepartementController {

    private final DepartementService departementService;

    // GET /api/Departements -> tous les métiers
    @GetMapping
    public List<Departement> getAllDepartements() {
        return departementService.getAll();
    }

    // GET /api/Departements/{id} -> métier par ID
    @GetMapping("/{id}")
    public Departement getDepartementById(@PathVariable Integer id) {
        return departementService.getById(id);
    }
}
