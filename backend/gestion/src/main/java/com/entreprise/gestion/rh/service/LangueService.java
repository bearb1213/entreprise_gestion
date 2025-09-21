package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.model.Langue;
import com.entreprise.gestion.rh.repository.LangueRepository;
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
        List<Langue> langues= langueRepository.findAll();
        for(Langue langue : langues) {
            langue.setBesoinLangues(null);
        }
        return langues;
    }

    public Langue getById(Integer id) {
        return langueRepository.findById(id).orElse(null);
    }
}
