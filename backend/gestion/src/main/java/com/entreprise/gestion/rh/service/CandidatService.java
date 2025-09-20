package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.model.*;
import com.entreprise.gestion.rh.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

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
    
    // 1. Validation de la date de naissance
    if (candidat.getPersonne().getDateNaissance() == null) {
        return 0; // ou gérer l'erreur
    }
    
    // 2. Calcul d'âge sécurisé
    LocalDate now = LocalDate.now();
    int age = Period.between(candidat.getPersonne().getDateNaissance(), now).getYears();
    
    // Points pour l'âge
    if (age >= besoin.getMinAge() && age <= besoin.getMaxAge()) {
        score += besoin.getCoeffAge();
    }
    
    // 3. Points pour l'expérience (corrigé)
    if (candidat.getExperiences() != null) {
        int totalExperience = candidat.getExperiences().stream()
            .mapToInt(Experience::getNbAnnee)
            .sum();
        
        if (totalExperience >= besoin.getMinExperience()) {
            // Formule plus raisonnable
            int experienceBonus = Math.min(totalExperience - besoin.getMinExperience(), 10);
            score += besoin.getCoeffExperience() * experienceBonus;
        }
    }
    
    // 4. Optimisation des compétences avec Map
    if (candidat.getCompetences() != null && besoin.getBesoinCompetences() != null) {
        Map<Integer, Integer> besoinCompetencesMap = besoin.getBesoinCompetences().stream()
            .collect(Collectors.toMap(
                bc -> bc.getCompetence().getId(),
                BesoinCompetence::getCoeff
            ));
        
        for (CandidatCompetence cc : candidat.getCompetences()) {
            Integer coeff = besoinCompetencesMap.get(cc.getCompetence().getId());
            if (coeff != null) {
                score += coeff;
            }
        }
    }
    
    // 5. Même optimisation pour les langues
    if (candidat.getLangues() != null && besoin.getBesoinLangues() != null) {
        Map<Integer, Integer> besoinLanguesMap = besoin.getBesoinLangues().stream()
            .collect(Collectors.toMap(
                bl -> bl.getLangue().getId(),
                BesoinLangue::getCoeff
            ));
        
        for (CandidatLangue cl : candidat.getLangues()) {
            Integer coeff = besoinLanguesMap.get(cl.getLangue().getId());
            if (coeff != null) {
                score += coeff;
            }
        }
    }
    
    // 6. Même optimisation pour les diplômes
    if (candidat.getDiplomes() != null && besoin.getBesoinDiplomeFilieres() != null) {
        Map<Integer, Integer> besoinDiplomesMap = besoin.getBesoinDiplomeFilieres().stream()
            .collect(Collectors.toMap(
                bdf -> bdf.getDiplomeFiliere().getId(),
                BesoinDiplomeFiliere::getCoeff
            ));
        
        for (CandidatDiplomeFiliere cdf : candidat.getDiplomes()) {
            Integer coeff = besoinDiplomesMap.get(cdf.getDiplomeFiliere().getId());
            if (coeff != null) {
                score += coeff;
            }
        }
    }
    
    return score;
}
    
    public List<Besoin> getActiveBesoins() {
        return besoinRepository.findByStatut( 1);
    }
   
}