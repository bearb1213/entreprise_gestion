package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.rh.dto.BesoinDTO;
import com.entreprise.gestion.rh.dto.BesoinRequest;
import com.entreprise.gestion.rh.model.Besoin;
import com.entreprise.gestion.rh.service.BesoinService;
import com.entreprise.gestion.rh.service.CompetenceService;
import com.entreprise.gestion.rh.service.DepartementService;
import com.entreprise.gestion.rh.service.DiplomeFiliereService;
import com.entreprise.gestion.rh.service.LangueService;
import com.entreprise.gestion.rh.service.MetierService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/besoins")
public class BesoinController {

    private final BesoinService besoinService;
    private final MetierService metierService;
    private final DepartementService departementService;
    private final CompetenceService competenceService;
    private final LangueService langueService;
    private final DiplomeFiliereService diplomeFiliereService;

    public BesoinController(BesoinService besoinService,
                            MetierService metierService,
                            DepartementService departementService,
                            CompetenceService competenceService,
                            LangueService langueService,
                            DiplomeFiliereService diplomeFiliereService) {
        this.besoinService = besoinService;
        this.metierService = metierService;
        this.departementService = departementService;
        this.competenceService = competenceService;
        this.langueService = langueService;
        this.diplomeFiliereService = diplomeFiliereService;
    }

    @GetMapping
    public List<Besoin> listBesoins() {
        return besoinService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Besoin> getBesoin(@PathVariable Integer id) {
        return besoinService.findById(id);
    }

    @GetMapping("/metiers")
    public Object getMetiers() {
        return metierService.getAll();
    }

    @GetMapping("/departements")
    public Object getDepartements() {
        return departementService.getAllDepartements();
    }

    @GetMapping("/competences")
    public Object getCompetences() {
        return competenceService.getAll();
    }

    @GetMapping("/langues")
    public Object getLangues() {
        return langueService.getAll();
    }

    @GetMapping("/diplomes-filieres")
    public Object getDiplomeFilieres() {
        return diplomeFiliereService.findAll();
    }

    @GetMapping("/besoins")
    public ResponseEntity<List<BesoinDTO>> getAllBesoins() {
        return ResponseEntity.ok(besoinService.getAllBesoins());
    }

    @PostMapping
    public ResponseEntity<Besoin> createBesoin(@RequestBody BesoinRequest request) {
        Besoin besoin = besoinService.createBesoin(request);
        return ResponseEntity.ok(besoin);
    }

}