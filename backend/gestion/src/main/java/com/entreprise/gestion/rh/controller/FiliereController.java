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
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/filieres")
public class FiliereController {

    private final FiliereService filiereService;
    private final DiplomeFiliereService diplomeFiliereService;

    // GET {id}/diplomes
    @GetMapping("{id}/diplomes")
    public Map<Integer, String> getDiplomesByFiliere(@PathVariable Integer id) {
        Filiere filiere = filiereService.getById(id);
        if (filiere != null) {
            List<DiplomeFiliere> liaisons = diplomeFiliereService.getDiplomesByFiliere(filiere);

            return liaisons.stream()
                    .collect(Collectors.toMap(
                            DiplomeFiliere::getId,          // clé : id de la liaison
                            df -> df.getDiplome().getLibelle() // valeur : nom du diplôme
                    ));
        }
        return Map.of(); // Map vide si filière non trouvée
    }

    @GetMapping
    public List<Filiere> getAllFilieres() {
        return filiereService.getAll();
    }

}
