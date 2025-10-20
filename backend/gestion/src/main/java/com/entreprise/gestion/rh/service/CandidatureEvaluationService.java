package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.model.*;
import com.entreprise.gestion.rh.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidatureEvaluationService {
    
    private final CandidatureRepository candidatureRepository;
    private final NotesRepository notesRepository;
    private final EvaluationRepository evaluationRepository;
    private final CandidatRepository candidatRepository;

    @Transactional
    public void evaluerCandidature(Integer candidatureId) throws Exception {
        Candidature candidature = candidatureRepository.findById(candidatureId)
                .orElseThrow(() -> new RuntimeException("Candidature non trouvée"));
        
        // Récupérer le besoin associé
        Besoin besoin = candidature.getBesoin();
        Candidat candidat = candidature.getCandidat();
        
        // Calculer la note totale basée sur les coefficients
        Double noteTotale = calculerNoteCandidature(candidat, besoin);
        
        // Utiliser l'évaluation avec ID=1
        Evaluation evaluation = evaluationRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Évaluation avec ID=1 non trouvée"));
        
        // Créer la note
        Notes note = new Notes();
        note.setNote(noteTotale);
        note.setCandidature(candidature);
        note.setEvaluation(evaluation);
        note.setDateEntree(LocalDateTime.now());
        
        notesRepository.save(note);
    }

    public Double calculerNoteCandidature(Candidat candidat, Besoin besoin) {
        Double noteTotale = 0.0;
        int totalCoefficients = 0;
        
        // 1. Évaluation par âge (coefficient du besoin)
        if (besoin.getMinAge() != null && besoin.getMaxAge() != null && besoin.getCoeffAge() != null) {
            Integer ageCandidat = calculerAge(candidat.getPersonne().getDateNaissance());
            if (ageCandidat >= besoin.getMinAge() && ageCandidat <= besoin.getMaxAge()) {
                noteTotale += besoin.getCoeffAge(); // Ajoute seulement le coefficient
            }
            totalCoefficients += besoin.getCoeffAge();
        }
        
        // 2. Évaluation par expérience (coefficient du besoin)
        if (besoin.getMinExperience() != null && besoin.getCoeffExperience() != null) {
            Integer experienceCandidat = calculerExperienceTotale(candidat);
            if (experienceCandidat >= besoin.getMinExperience()) {
                noteTotale += besoin.getCoeffExperience(); // Ajoute seulement le coefficient
            }
            totalCoefficients += besoin.getCoeffExperience();
        }
        
        // 3. Évaluation des compétences (coefficients des besoins_competences)
        if (besoin.getBesoinCompetences() != null) {
            for (BesoinCompetence besoinCompetence : besoin.getBesoinCompetences()) {
                boolean hasCompetence = candidat.getCompetences().stream()
                        .anyMatch(comp -> comp.getId().equals(besoinCompetence.getCompetence().getId()));
                if (hasCompetence) {
                    noteTotale += besoinCompetence.getCoeff(); // Ajoute seulement le coefficient
                }
                totalCoefficients += besoinCompetence.getCoeff();
            }
        }
        
        // 4. Évaluation des langues (coefficients des besoins_langues)
        if (besoin.getBesoinLangues() != null) {
            for (BesoinLangue besoinLangue : besoin.getBesoinLangues()) {
                boolean hasLangue = candidat.getLangues().stream()
                        .anyMatch(lang -> lang.getId().equals(besoinLangue.getLangue().getId()));
                if (hasLangue) {
                    noteTotale += besoinLangue.getCoeff(); // Ajoute seulement le coefficient
                }
                totalCoefficients += besoinLangue.getCoeff();
            }
        }
        
        // 5. Évaluation des diplômes/filières (coefficients des besoins_diplome_filiere)
        if (besoin.getBesoinDiplomeFilieres() != null) {
            for (BesoinDiplomeFiliere besoinDiplome : besoin.getBesoinDiplomeFilieres()) {
                boolean hasDiplome = candidat.getDiplomeFilieres().stream()
                        .anyMatch(diplome -> diplome.getId().equals(besoinDiplome.getDiplomeFiliere().getId()));
                if (hasDiplome) {
                    noteTotale += besoinDiplome.getCoeff(); // Ajoute seulement le coefficient
                }
                totalCoefficients += besoinDiplome.getCoeff();
            }
        }
        
        // Calcul de la note finale (somme des coefficients validés)
        return noteTotale;
    }

    private Integer calculerAge(LocalDate dateNaissance) {
        if (dateNaissance == null) {
            return 0;
        }
        return Period.between(dateNaissance, LocalDate.now()).getYears();
    }



    private Integer calculerExperienceTotale(Candidat candidat) {
        if (candidat.getExperiences() != null && !candidat.getExperiences().isEmpty()) {
            return candidat.getExperiences().stream()
                    .mapToInt(experience -> experience.getNbAnnee() != null ? experience.getNbAnnee() : 0)
                    .sum();
        }
        return 0;
    }

}