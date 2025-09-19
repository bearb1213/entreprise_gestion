package com.entreprise.gestion.rh.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.entreprise.gestion.rh.model.BesoinDiplomeFiliere;
import com.entreprise.gestion.rh.repository.BesoinDiplomeFiliereRepository;

public class BesoinDiplomeFiliereService {
    @Autowired
    private BesoinDiplomeFiliereRepository besoinDiplomeFiliereRepository;

    public List<BesoinDiplomeFiliere> findAll() { return besoinDiplomeFiliereRepository.findAll(); }
    public Optional<BesoinDiplomeFiliere> findById(Long id) { return besoinDiplomeFiliereRepository.findById(id); }
    public BesoinDiplomeFiliere save(BesoinDiplomeFiliere bdf) { return besoinDiplomeFiliereRepository.save(bdf); }
}