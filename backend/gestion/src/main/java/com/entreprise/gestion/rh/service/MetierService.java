package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.model.Metier;
import com.entreprise.gestion.rh.repository.MetierRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MetierService {

    private final MetierRepository metierRepository;

    public MetierService(MetierRepository metierRepository) {
        this.metierRepository = metierRepository;
    }

    public List<Metier> getAll() {
        return metierRepository.findAll();
    }

    public Metier getById(Integer id) {
        return metierRepository.findById(id).orElse(null);
    }

}
