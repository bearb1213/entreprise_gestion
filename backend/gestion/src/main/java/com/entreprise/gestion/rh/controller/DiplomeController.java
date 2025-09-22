package com.entreprise.gestion.rh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.entreprise.gestion.rh.service.DiplomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import com.entreprise.gestion.rh.dto.DiplomeDTO;
import java.util.List;
import java.util.stream.Collectors;
import com.entreprise.gestion.rh.model.Diplome;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/diplomes")

public class DiplomeController {
    @Autowired
    private DiplomeService diplomeService;

    @PreAuthorize("hasRole('isAuthenticated'")
    @GetMapping
    public ResponseEntity<List<DiplomeDTO>> getAllDiplomes() {
        List<DiplomeDTO> diplomes = diplomeService.getAll().stream()
                .map(dip -> new DiplomeDTO(dip.getId(), dip.getLibelle()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(diplomes);
    }

    @PreAuthorize("hasRole('isAuthenticated'")
    @GetMapping("/{id}")
    public ResponseEntity<DiplomeDTO> getDiplomeById(@PathVariable Integer id) {
        Diplome diplome = diplomeService.getById(id);
        if (diplome != null) {
            DiplomeDTO dto = new DiplomeDTO(diplome.getId(), diplome.getLibelle());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }
    @PreAuthorize("hasRole('Admin' or hasRole('Rh') or hasRole('Departement')")
    @PostMapping
    public ResponseEntity<DiplomeDTO> createDiplome(@RequestBody Diplome diplome) {
        Diplome createdDiplome = diplomeService.createDiplome(diplome);
        DiplomeDTO dto = new DiplomeDTO(createdDiplome.getId(), createdDiplome.getLibelle());
        return ResponseEntity.ok(dto);
    }
    @PreAuthorize("hasRole('Admin' or hasRole('Rh') or hasRole('Departement')")
    @PutMapping("/{id}")
    public ResponseEntity<DiplomeDTO> updateDiplome(@PathVariable Integer id, @RequestBody Diplome diplome) {
        // Assuming you have an update method in your service
        Diplome updatedDiplome = diplomeService.updateDiplome(id, diplome);
        DiplomeDTO dto = new DiplomeDTO(updatedDiplome.getId(), updatedDiplome.getLibelle());
        return ResponseEntity.ok(dto);
    }


    
}
