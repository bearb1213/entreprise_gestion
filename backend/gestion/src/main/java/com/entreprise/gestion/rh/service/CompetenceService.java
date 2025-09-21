package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.model.Competence;
import com.entreprise.gestion.rh.repository.CompetenceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CompetenceService {

    private final CompetenceRepository competenceRepository;

    public CompetenceService(CompetenceRepository competenceRepository) {
        this.competenceRepository = competenceRepository;
    }

    public List<Competence> getAll() {
        List<Competence> competences= competenceRepository.findAll();
        for(Competence c : competences) {
            c.setCandidats(null);
            c.setBesoinCompetences(null);
        }
        return competences;
    }

    public Competence getById(Integer id) {
        return competenceRepository.findById(id).orElse(null);
    }

}
