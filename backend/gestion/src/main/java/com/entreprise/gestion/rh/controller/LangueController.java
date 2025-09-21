package com.entreprise.gestion.rh.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import com.entreprise.gestion.rh.service.LangueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import com.entreprise.gestion.rh.dto.LangueDTO;
import java.util.List;
import java.util.stream.Collectors;
import com.entreprise.gestion.rh.model.Langue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.access.prepost.PreAuthorize;


@RestController
@RequestMapping("/api/langues")
public class LangueController {
    @Autowired
    private LangueService langueService;

    @PreAuthorize("hasRole('isAuthenticated'")
    @GetMapping
   public ResponseEntity<List<LangueDTO>> getAllLangues() {
       List<LangueDTO> langues = langueService.getAll().stream()
               .map(lang -> new LangueDTO(lang.getId(), lang.getLibelle()))
               .collect(Collectors.toList());
       return ResponseEntity.ok(langues);
   }

    @PreAuthorize("hasRole('isAuthenticated'")
   @GetMapping("/{id}")
   public ResponseEntity<LangueDTO> getLangueById(@PathVariable Integer id) {
       Langue langue = langueService.getById(id);
       if (langue != null) {
              LangueDTO dto = new LangueDTO(langue.getId(), langue.getLibelle());
              return ResponseEntity.ok(dto);
         }
         return ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('Admin' or hasRole('Rh') or hasRole('Departement')")
    @PostMapping
    public ResponseEntity<LangueDTO> createLangue(@RequestBody Langue langue) {
        Langue createdLangue = langueService.createLangue(langue);
        LangueDTO dto = new LangueDTO(createdLangue.getId(), createdLangue.getLibelle());
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('Admin' or hasRole('Rh') or hasRole('Departement')")
    @PutMapping("/{id}")
    public ResponseEntity<LangueDTO> updateLangue(@PathVariable Integer id, @RequestBody Langue langue) {
        // Assuming you have an update method in your service
        Langue updatedLangue = langueService.updateLangue(id, langue);
        LangueDTO dto = new LangueDTO(updatedLangue.getId(), updatedLangue.getLibelle());
        return ResponseEntity.ok(dto);  
    }

    
}
