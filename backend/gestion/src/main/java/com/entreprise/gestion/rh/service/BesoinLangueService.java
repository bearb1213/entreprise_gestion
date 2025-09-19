package com.entreprise.gestion.rh.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entreprise.gestion.rh.model.BesoinLangue;
import com.entreprise.gestion.rh.repository.BesoinLangueRepository;

@Service
public class BesoinLangueService {
    @Autowired
    private BesoinLangueRepository besoinLangueRepository;

    public List<BesoinLangue> findAll() { return besoinLangueRepository.findAll(); }
    public Optional<BesoinLangue> findById(Integer id) { return besoinLangueRepository.findById(id); }
    public BesoinLangue save(BesoinLangue bl) { return besoinLangueRepository.save(bl); }
}
