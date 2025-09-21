package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.rh.model.Besoin;
import com.entreprise.gestion.rh.dto.BesoinDTO;
import com.entreprise.gestion.rh.service.BesoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/besoins")
public class BesoinController {

    @Autowired
    private BesoinService besoinService;

    /**
     * Récupère tous les besoins
     */
    @PreAuthorize("hasRole('isAuthenticated'")
    @GetMapping
    public ResponseEntity<List<BesoinDTO>> getAllBesoins() {
        List<BesoinDTO> besoins = besoinService.getAllBesoins();
        return ResponseEntity.ok(besoins);
    }

    /**
     * Récupère un besoin par son ID
     */
    @PreAuthorize("hasRole('isAuthenticated'")
    @GetMapping("/{id}")
    public ResponseEntity<BesoinDTO> getBesoinById(@PathVariable Integer id) {
        BesoinDTO besoin = besoinService.getBesoinById(id);
        if (besoin != null) {
            return ResponseEntity.ok(besoin);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Récupère les besoins actifs (statut = 1)
     */
    @PreAuthorize("hasRole('isAuthenticated'")
    @GetMapping("/actifs")
    public ResponseEntity<List<BesoinDTO>> getBesoinsActifs() {
        List<BesoinDTO> besoins = besoinService.getBesoinsActifs();
        return ResponseEntity.ok(besoins);
    }

    /**
     * Récupère les besoins par métier
     */
    @PreAuthorize("hasRole('isAuthenticated'")
    @GetMapping("/metier/{metierId}")
    public ResponseEntity<List<BesoinDTO>> getBesoinsByMetier(@PathVariable Integer metierId) {
        List<BesoinDTO> besoins = besoinService.getBesoinsByMetier(metierId);
        return ResponseEntity.ok(besoins);
    }

    /**
     * Récupère les besoins par département
     */
    @PreAuthorize("hasRole('isAuthenticated'")
    @GetMapping("/departement/{departementId}")
    public ResponseEntity<List<BesoinDTO>> getBesoinsByDepartement(@PathVariable Integer departementId) {
        List<BesoinDTO> besoins = besoinService.getBesoinsByDepartement(departementId);
        return ResponseEntity.ok(besoins);
    }

    /**
     * Crée un nouveau besoin
     */
    @PreAuthorize("hasRole('Admin' or hasRole('Rh') or hasRole('Departement')")
    @PostMapping
    public ResponseEntity<BesoinDTO> createBesoin(@RequestBody BesoinDTO besoinDTO) {
        BesoinDTO createdBesoin = besoinService.createBesoin(besoinDTO);
        return ResponseEntity.ok(createdBesoin);
    }

    /**
     * Met à jour un besoin existant
     */
    @PreAuthorize("hasRole('Admin' or hasRole('Rh') or hasRole('Departement')")
    @PutMapping("/{id}")
    public ResponseEntity<BesoinDTO> updateBesoin(@PathVariable Integer id, @RequestBody BesoinDTO besoinDTO) {
        BesoinDTO updatedBesoin = besoinService.updateBesoin(id, besoinDTO);
        if (updatedBesoin != null) {
            return ResponseEntity.ok(updatedBesoin);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Supprime un besoin
     */
    @PreAuthorize("hasRole('Admin' or hasRole('Rh') or hasRole('Departement')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBesoin(@PathVariable Integer id) {
        boolean deleted = besoinService.deleteBesoin(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Change le statut d'un besoin
     */
    @PreAuthorize("hasRole('Admin' or hasRole('Rh') or hasRole('Departement')")
    @PatchMapping("/{id}/statut")
    public ResponseEntity<BesoinDTO> updateStatut(@PathVariable Integer id, @RequestParam Integer statut) {
        BesoinDTO updatedBesoin = besoinService.updateStatut(id, statut);
        if (updatedBesoin != null) {
            return ResponseEntity.ok(updatedBesoin);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Endpoint public pour récupérer les besoins actifs
     */
    @PreAuthorize("hasRole('Admin' or hasRole('Rh') or hasRole('Departement')")
    @GetMapping("/public/actifs")
    public ResponseEntity<List<BesoinDTO>> getBesoinsActifsPublic() {
        List<BesoinDTO> besoins = besoinService.getBesoinsActifs();
        return ResponseEntity.ok(besoins);
    }

    /**
     * Recherche des besoins avec critères multiples
     */
    @PreAuthorize("hasRole('isAuthenticated'")
    @GetMapping("/recherche")
    public ResponseEntity<List<BesoinDTO>> searchBesoins(
            @RequestParam(required = false) Integer metierId,
            @RequestParam(required = false) Integer departementId,
            @RequestParam(required = false) Integer statut,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge) {
        
        List<BesoinDTO> besoins = besoinService.searchBesoins(metierId, departementId, statut, minAge, maxAge);
        return ResponseEntity.ok(besoins);
    }
}