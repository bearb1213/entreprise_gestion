package com.entreprise.gestion.rh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entreprise.gestion.rh.model.ReponseCandidat;
import com.entreprise.gestion.rh.repository.ReponseCandidatRepository;

@Service
public class ReponseCandidatService {
    @Autowired
    private ReponseCandidatRepository reponseCandidatRepository;


    public ReponseCandidat saveReponseCandidat(ReponseCandidat reponseCandidat)
    {
        return reponseCandidatRepository.save(reponseCandidat);
    }
    
}
