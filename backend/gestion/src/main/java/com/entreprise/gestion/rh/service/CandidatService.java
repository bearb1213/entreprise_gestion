package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.model.*;
import com.entreprise.gestion.rh.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidatService {
    
    private final CandidatRepository candidatRepository;
    private final PersonneRepository personneRepository;
    private final BesoinRepository besoinRepository;
    private final CompetenceRepository competenceRepository;
    private final LangueRepository langueRepository;
    private final DiplomeFiliereRepository diplomeFiliereRepository;
    
    @Transactional
    public Candidat saveCandidat(Candidat candidat) {
        // Sauvegarder d'abord la personne
        Personne personne = personneRepository.save(candidat.getPersonne());
        candidat.setPersonne(personne);
        
        return candidatRepository.save(candidat);
    }
    
    public int calculateScore(Candidat candidat, Besoin besoin) {
        int score = 0;
        
        // Calculer l'âge
        LocalDate now = LocalDate.now();
        int age = Period.between(candidat.getPersonne().getDateNaissance(), now).getYears();
        
        // Points pour l'âge
        if (age >= besoin.getMinAge() && age <= besoin.getMaxAge()) {
            score += besoin.getCoeffAge();
        }
        
        // Points pour l'expérience
        if (candidat.getExperiences() != null) {
            int totalExperience = candidat.getExperiences().stream()
                .mapToInt(Experience::getNbAnnee)
                .sum();
            
            if (totalExperience >= besoin.getMinExperience()) {
                score += besoin.getCoeffExperience() * (totalExperience - besoin.getMinExperience() + 1);
            }
        }
        
        // Points pour les compétences
        if (candidat.getCompetences() != null && besoin.getBesoinCompetences() != null) {
            for (CandidatCompetence cc : candidat.getCompetences()) {
                for (BesoinCompetence bc : besoin.getBesoinCompetences()) {
                    if (cc.getCompetence().getId().equals(bc.getCompetence().getId())) {
                        score += bc.getCoeff();
                    }
                }
            }
        }
        
        // Points pour les langues
        if (candidat.getLangues() != null && besoin.getBesoinLangues() != null) {
            for (CandidatLangue cl : candidat.getLangues()) {
                for (BesoinLangue bl : besoin.getBesoinLangues()) {
                    if (cl.getLangue().getId().equals(bl.getLangue().getId())) {
                        score += bl.getCoeff();
                    }
                }
            }
        }
        
        // Points pour les diplômes
        if (candidat.getDiplomes() != null && besoin.getBesoinDiplomeFilieres() != null) {
            for (CandidatDiplomeFiliere cdf : candidat.getDiplomes()) {
                for (BesoinDiplomeFiliere bdf : besoin.getBesoinDiplomeFilieres()) {
                    if (cdf.getDiplomeFiliere().getId().equals(bdf.getDiplomeFiliere().getId())) {
                        score += bdf.getCoeff();
                    }
                }
            }
        }
        
        return score;
    }
    
    public List<Besoin> getActiveBesoins() {
        return besoinRepository.findByStatut( 1);
    }
   
}