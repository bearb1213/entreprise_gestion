package com.entreprise.gestion.rh.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entreprise.gestion.rh.model.BesoinCompetence;
import com.entreprise.gestion.rh.repository.BesoinCompetenceRepository;

@Service
public class BesoinCompetenceService {
    @Autowired
    private BesoinCompetenceRepository besoinCompetenceRepository;

    public List<BesoinCompetence> findAll() { return besoinCompetenceRepository.findAll(); }
    public Optional<BesoinCompetence> findById(Long id) { return besoinCompetenceRepository.findById(id); }
    public BesoinCompetence save(BesoinCompetence bc) { return besoinCompetenceRepository.save(bc); }
}