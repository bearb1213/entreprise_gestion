package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.rh.model.Diplome;
import com.entreprise.gestion.rh.model.DiplomeFiliere;
import com.entreprise.gestion.rh.model.Filiere;
import com.entreprise.gestion.rh.service.DiplomeFiliereService;
import com.entreprise.gestion.rh.service.FiliereService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/filieres")
public class FiliereController {

    private final FiliereService filiereService;
    private final DiplomeFiliereService diplomeFiliereService;


    @GetMapping("{id}/diplomes")
    public List<Diplome> getDiplomesByFiliere(@PathVariable Integer id) {
        Filiere filiere = filiereService.getById(id);
        if (filiere != null) {
            List<DiplomeFiliere> liaisons = diplomeFiliereService.getDiplomesByFiliere(filiere);
            List<Diplome> diplomes = new ArrayList<>();
            for (int i =0 ; i<liaisons.size(); i++) {
                Diplome d= liaisons.get(i).getDiplome();
                d.setId(liaisons.get(i).getId());
                d.setDiplomeFilieres(null);
                
                diplomes.add(d);
            }

            return diplomes;
        }
        return List.of(); // Liste vide si filière non trouvée
    }





    @GetMapping
    public List<Filiere> getAllFilieres() {
        return filiereService.getAll();
    }

}
