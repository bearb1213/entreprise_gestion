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
    
        LocalDate now = LocalDate.now();
    
        // 1. ÂGE
        if (candidat.getPersonne().getDateNaissance() != null 
            && besoin.getMinAge() != null && besoin.getMaxAge() != null) {
            
            int age = Period.between(candidat.getPersonne().getDateNaissance(), now).getYears();
            
            int ageNote = (age >= besoin.getMinAge() && age <= besoin.getMaxAge()) ? 20 : 0;
            score += ageNote * besoin.getCoeffAge();
        }
    
        // 2. EXPÉRIENCE
        if (candidat.getExperiences() != null && besoin.getMinExperience() != null) {
            int totalExp = candidat.getExperiences().stream()
                    .mapToInt(Experience::getNbAnnee)
                    .sum();
    
            // formule : (exp / minExp) * 20, plafonné à 20
            int expNote = (int) Math.min((totalExp * 20.0) / besoin.getMinExperience(), 20);
            score += expNote * besoin.getCoeffExperience();
        }
    
        // 3. COMPÉTENCES
        if (candidat.getCompetences() != null && besoin.getBesoinCompetences() != null) {
            Map<Integer, Integer> besoinCompetencesMap = besoin.getBesoinCompetences().stream()
                .collect(Collectors.toMap(
                    bc -> bc.getCompetence().getId(),
                    BesoinCompetence::getCoeff
                ));
    
            for (BesoinCompetence bc : besoin.getBesoinCompetences()) {
                boolean hasSkill = candidat.getCompetences().stream()
                    .anyMatch(cc -> cc.getCompetence().getId().equals(bc.getCompetence().getId()));
    
                int skillNote = hasSkill ? 20 : 0;
                score += skillNote * bc.getCoeff();
            }
        }
    
        // 4. LANGUES
        if (candidat.getLangues() != null && besoin.getBesoinLangues() != null) {
            for (BesoinLangue bl : besoin.getBesoinLangues()) {
                boolean hasLang = candidat.getLangues().stream()
                    .anyMatch(cl -> cl.getLangue().getId().equals(bl.getLangue().getId()));
    
                int langNote = hasLang ? 20 : 0;
                score += langNote * bl.getCoeff();
            }
        }
    
        // 5. DIPLÔMES
        if (candidat.getDiplomes() != null && besoin.getBesoinDiplomeFilieres() != null) {
            for (BesoinDiplomeFiliere bdf : besoin.getBesoinDiplomeFilieres()) {
                boolean hasDiplome = candidat.getDiplomes().stream()
                    .anyMatch(cdf -> cdf.getDiplomeFiliere().getId().equals(bdf.getDiplomeFiliere().getId()));
    
                int diplomeNote = hasDiplome ? 20 : 0;
                score += diplomeNote * bdf.getCoeff();
            }
        }
    
        return score;
    }
    
    public List<Besoin> getActiveBesoins() {
        return besoinRepository.findByStatut( 1);
    }
   
}