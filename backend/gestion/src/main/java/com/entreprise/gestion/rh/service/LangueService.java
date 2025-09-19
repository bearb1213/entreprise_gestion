package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.model.Langue;
import com.entreprise.gestion.rh.repository.LangueRepository;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LangueService {

    private final LangueRepository langueRepository;

    public LangueService(LangueRepository langueRepository) {
        this.langueRepository = langueRepository;
    }

    public List<Langue> getAll() {
        return langueRepository.findAll();
    }

    public Langue getById(Integer id) {
        return langueRepository.findById(id).orElse(null);
    }

    public Map<String, Object> getAllMap() {
        return langueRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        l -> String.valueOf(l.getId()),
                        l -> l.getLibelle()
                ));
    }

}
