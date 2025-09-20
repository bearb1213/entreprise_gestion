package com.entreprise.gestion.rh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.entreprise.gestion.rh.service.MetierService;
import org.springframework.http.ResponseEntity;
import com.entreprise.gestion.rh.dto.MetierDTO;
import java.util.List;
import java.util.stream.Collectors;
import com.entreprise.gestion.rh.model.Metier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/metiers")

public class MetierController {
    @Autowired
    private MetierService metierService;

    @GetMapping
    public ResponseEntity<List<MetierDTO>> getAllMetiers() {
        List<MetierDTO> metiers = metierService.getAll().stream()
                .map(met -> new MetierDTO(met.getId(), met.getLibelle()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(metiers);
    }
    @GetMapping("/{id}")
    public ResponseEntity<MetierDTO> getMetierById(@PathVariable Integer id) {
        Metier metier = metierService.getById(id);
        if (metier != null) {
            MetierDTO dto = new MetierDTO(metier.getId(), metier.getLibelle());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<MetierDTO> createMetier(@RequestBody Metier metier) {
        Metier createdMetier = metierService.createMetier(metier);
        MetierDTO dto = new MetierDTO(createdMetier.getId(), createdMetier.getLibelle());
        return ResponseEntity.ok(dto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<MetierDTO> updateMetier(@PathVariable Integer id, @RequestBody Metier metier) {
        // Assuming you have an update method in your service
        Metier updatedMetier = metierService.updateMetier(id, metier);
        MetierDTO dto = new MetierDTO(updatedMetier.getId(), updatedMetier.getLibelle());
        return ResponseEntity.ok(dto);  
    }

}
