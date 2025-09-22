package com.entreprise.gestion.rh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entreprise.gestion.exception.MyException;
import com.entreprise.gestion.rh.model.Candidature;
import com.entreprise.gestion.rh.repository.CandidatureRepository;

@Service
public class CandidatureService {

    @Autowired
    private CandidatureRepository candidatureRepository;

    public Candidature findCandidatureById(Integer id) throws Exception
    {
        return candidatureRepository.findById(id).orElseThrow(()->new MyException("Candidature introuvable"));
    }
    

}
