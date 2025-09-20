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
        return competenceRepository.findAll();
    }

    public Competence getById(Integer id) {
        return competenceRepository.findById(id).orElse(null);
    }

    public Map<String, Object> getAllMap() {
        return competenceRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        c -> String.valueOf(c.getId()),
                        c -> c.getLibelle()
                ));
    }
    public Competence createCompetence(Competence competence) {
        return competenceRepository.save(competence);
    }
    public Competence updateCompetence(Integer id, Competence competence) {
        competence.setId(id);
        return competenceRepository.save(competence);
    }
    public void deleteCompetence(Integer id) {
        competenceRepository.deleteById(id);
    }
}
