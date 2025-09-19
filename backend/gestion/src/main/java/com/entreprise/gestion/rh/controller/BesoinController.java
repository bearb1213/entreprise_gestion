package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.rh.model.Besoin;
import com.entreprise.gestion.rh.service.BesoinService;
import com.entreprise.gestion.rh.service.CompetenceService;
import com.entreprise.gestion.rh.service.DepartementService;
import com.entreprise.gestion.rh.service.DiplomeFiliereService;
import com.entreprise.gestion.rh.service.LangueService;
import com.entreprise.gestion.rh.service.MetierService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/besoins")
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
    public String listBesoins(Model model) {
        model.addAttribute("besoins", besoinService.findAll());
        return "besoin/list"; 
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("besoin", new Besoin());
        model.addAttribute("metiers", metierService.getAll());
        model.addAttribute("departements", departementService.getAllDepartements());
        model.addAttribute("competences", competenceService.getAll());
        model.addAttribute("langues", langueService.getAll());
        model.addAttribute("diplomeFilieres", diplomeFiliereService.findAll());
        return "besoin/create"; // -> resources/templates/besoin/create.html
    }

    @PostMapping("/save")
    public String saveBesoin(@ModelAttribute Besoin besoin) {
        besoinService.save(besoin);
        return "redirect:/besoins";
    }
}
