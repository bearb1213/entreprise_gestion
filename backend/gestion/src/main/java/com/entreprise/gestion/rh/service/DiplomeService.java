package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.model.Diplome;
import com.entreprise.gestion.rh.repository.DiplomeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DiplomeService {

    private final DiplomeRepository diplomeRepository;

    public DiplomeService(DiplomeRepository diplomeRepository) {
        this.diplomeRepository = diplomeRepository;
    }

    public List<Diplome> getAll() {
        return diplomeRepository.findAll();
    }

    public Diplome getById(Integer id) {
        return diplomeRepository.findById(id).orElse(null);
    }

    public Map<String, Object> getAllMap() {
        return diplomeRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        d -> String.valueOf(d.getId()),
                        d -> d.getLibelle()
                ));
    }
    public Diplome createDiplome(Diplome diplome) {
        return diplomeRepository.save(diplome);
    }
    public Diplome updateDiplome(Integer id, Diplome diplome) {
        diplome.setId(id);
        return diplomeRepository.save(diplome);
    }
}
