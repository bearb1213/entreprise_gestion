package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.model.Departement;
import com.entreprise.gestion.rh.repository.DepartementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartementService {

    private final DepartementRepository departementRepository;

    public DepartementService(DepartementRepository DepartementRepository) {
        this.departementRepository = DepartementRepository;
    }

    public List<Departement> getAll() {
        List<Departement> departements = departementRepository.findAll();
        for (Departement departement : departements) {
            departement.setBesoins(null);
            departement.setUtilisateurs(null);
            departement.setQuestions(null);
        }
        return departements;
    }

    public Departement getById(Integer id) {
        return departementRepository.findById(id).orElse(null);
    }

}
