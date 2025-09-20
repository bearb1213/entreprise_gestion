package com.entreprise.gestion.rh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entreprise.gestion.rh.model.Choix;
import com.entreprise.gestion.rh.repository.ChoixRepository;

@Service
public class ChoixService {
    @Autowired
    private ChoixRepository choixRepository;
    
    public Choix findChoixById(Integer id)
    {
        return choixRepository.findById(id).orElse(null);
    }

    public Choix saveChoix(Choix choix)
    {
        return choixRepository.save(choix);
    }
}
