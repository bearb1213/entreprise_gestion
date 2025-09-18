package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.model.Filiere;
import com.entreprise.gestion.rh.repository.FiliereRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FiliereService {

    private final FiliereRepository filiereRepository;

    public FiliereService(FiliereRepository filiereRepository) {
        this.filiereRepository = filiereRepository;
    }

    public List<Filiere> getAll() {
        return filiereRepository.findAll();
    }

    public Filiere getById(Integer id) {
        return filiereRepository.findById(id).orElse(null);
    }

    public Map<String, Object> getAllMap() {
        return filiereRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        f -> String.valueOf(f.getId()),
                        f -> f.getLibelle()
                ));
    }
}
