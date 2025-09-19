package com.entreprise.gestion.rh.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entreprise.gestion.rh.model.Besoin;
import com.entreprise.gestion.rh.repository.BesoinRepository;

@Service
public class BesoinService {
    @Autowired
    private BesoinRepository besoinRepository;

    public List<Besoin> findAll() { return besoinRepository.findAll(); }
    public Optional<Besoin> findById(Long id) { return besoinRepository.findById(id); }
    public Besoin save(Besoin besoin) { return besoinRepository.save(besoin); }
}