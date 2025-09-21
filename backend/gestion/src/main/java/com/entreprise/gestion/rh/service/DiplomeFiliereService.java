package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.model.Diplome;
import com.entreprise.gestion.rh.model.DiplomeFiliere;
import com.entreprise.gestion.rh.model.Filiere;
import com.entreprise.gestion.rh.repository.DiplomeFiliereRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiplomeFiliereService {

    private final DiplomeFiliereRepository diplomeFiliereRepository;

    // Renvoie les diplômes liés à une filière
    public List<DiplomeFiliere> getDiplomesByFiliere(Filiere filiere) {
        return diplomeFiliereRepository.findByFiliere(filiere);
    }
}
