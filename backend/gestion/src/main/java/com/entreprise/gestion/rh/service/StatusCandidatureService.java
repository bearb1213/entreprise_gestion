package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.exception.MyException;
import com.entreprise.gestion.rh.model.StatusCandidature;
import com.entreprise.gestion.rh.model.Candidature;
import com.entreprise.gestion.rh.model.Evaluation;
import com.entreprise.gestion.rh.repository.StatusCandidatureRepository;
import com.entreprise.gestion.rh.repository.CandidatureRepository;
import com.entreprise.gestion.rh.repository.EvaluationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusCandidatureService {

    private final StatusCandidatureRepository statusCandidatureRepository;
    private final CandidatureRepository candidatureRepository;
    private final EvaluationRepository evaluationRepository;

    /**
     * Crée un nouveau statut de candidature
     */
    @Transactional
    public StatusCandidature createStatusCandidature(Integer candidatureId, Integer evaluationId) throws MyException {
        try {
            // Vérifier que la candidature existe
            Candidature candidature = candidatureRepository.findById(candidatureId)
                    .orElseThrow(() -> new MyException("Candidature introuvable avec id=" + candidatureId));

            // Vérifier que l'évaluation existe
            Evaluation evaluation = evaluationRepository.findById(evaluationId)
                    .orElseThrow(() -> new MyException("Évaluation introuvable avec id=" + evaluationId));

            // Créer le nouveau statut
            StatusCandidature statusCandidature = new StatusCandidature();
            statusCandidature.setCandidature(candidature);
            statusCandidature.setEvaluation(evaluation);
            statusCandidature.setDateEntree(LocalDateTime.now());

            return statusCandidatureRepository.save(statusCandidature);

        } catch (MyException e) {
            throw e;
        } catch (Exception e) {
            throw new MyException("Erreur lors de la création du statut de candidature", e);
        }
    }

    /**
     * Crée un nouveau statut de candidature avec date personnalisée
     */
    @Transactional
    public StatusCandidature createStatusCandidatureWithDate(Integer candidatureId, Integer evaluationId, LocalDateTime dateEntree) throws MyException {
        try {
            // Vérifier que la candidature existe
            Candidature candidature = candidatureRepository.findById(candidatureId)
                    .orElseThrow(() -> new MyException("Candidature introuvable avec id=" + candidatureId));

            // Vérifier que l'évaluation existe
            Evaluation evaluation = evaluationRepository.findById(evaluationId)
                    .orElseThrow(() -> new MyException("Évaluation introuvable avec id=" + evaluationId));

            // Créer le nouveau statut
            StatusCandidature statusCandidature = new StatusCandidature();
            statusCandidature.setCandidature(candidature);
            statusCandidature.setEvaluation(evaluation);
            statusCandidature.setDateEntree(dateEntree != null ? dateEntree : LocalDateTime.now());

            return statusCandidatureRepository.save(statusCandidature);

        } catch (MyException e) {
            throw e;
        } catch (Exception e) {
            throw new MyException("Erreur lors de la création du statut de candidature", e);
        }
    }

    /**
     * Récupère tous les statuts d'une candidature
     */
    @Transactional(readOnly = true)
    public List<StatusCandidature> getStatusByCandidature(Integer candidatureId) throws MyException {
        try {
            if (!candidatureRepository.existsById(candidatureId)) {
                throw new MyException("Candidature introuvable avec id=" + candidatureId);
            }
            return statusCandidatureRepository.findByCandidatureIdOrderByDateEntreeDesc(candidatureId);
        } catch (MyException e) {
            throw e;
        } catch (Exception e) {
            throw new MyException("Erreur lors de la récupération des statuts de candidature", e);
        }
    }

    /**
     * Récupère un statut par son ID
     */
    @Transactional(readOnly = true)
    public StatusCandidature getStatusById(Integer id) throws MyException {
        try {
            return statusCandidatureRepository.findById(id)
                    .orElseThrow(() -> new MyException("Statut de candidature introuvable avec id=" + id));
        } catch (MyException e) {
            throw e;
        } catch (Exception e) {
            throw new MyException("Erreur lors de la récupération du statut de candidature", e);
        }
    }

    /**
     * Supprime un statut de candidature
     */
    @Transactional
    public void deleteStatusCandidature(Integer id) throws MyException {
        try {
            if (!statusCandidatureRepository.existsById(id)) {
                throw new MyException("Statut de candidature introuvable avec id=" + id);
            }
            statusCandidatureRepository.deleteById(id);
        } catch (MyException e) {
            throw e;
        } catch (Exception e) {
            throw new MyException("Erreur lors de la suppression du statut de candidature", e);
        }
    }

    /**
     * Met à jour la date d'entrée d'un statut
     */
    @Transactional
    public StatusCandidature updateStatusDate(Integer id, LocalDateTime newDate) throws MyException {
        try {
            StatusCandidature statusCandidature = statusCandidatureRepository.findById(id)
                    .orElseThrow(() -> new MyException("Statut de candidature introuvable avec id=" + id));

            statusCandidature.setDateEntree(newDate);
            return statusCandidatureRepository.save(statusCandidature);

        } catch (MyException e) {
            throw e;
        } catch (Exception e) {
            throw new MyException("Erreur lors de la mise à jour du statut de candidature", e);
        }
    }

    /**
     * Vérifie si une candidature a déjà un statut pour une évaluation donnée
     */
    @Transactional(readOnly = true)
    public boolean existsByCandidatureAndEvaluation(Integer candidatureId, Integer evaluationId) throws MyException {
        try {
            List<StatusCandidature> statusList = statusCandidatureRepository.findByCandidatureId(candidatureId);
            return statusList.stream()
                    .anyMatch(status -> status.getEvaluation().getId().equals(evaluationId));
        } catch (Exception e) {
            throw new MyException("Erreur lors de la vérification du statut de candidature", e);
        }
    }
}