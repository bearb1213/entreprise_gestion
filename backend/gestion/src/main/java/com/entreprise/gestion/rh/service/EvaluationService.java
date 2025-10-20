package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.dto.EvaluationDTO;
import com.entreprise.gestion.rh.model.Evaluation;
import com.entreprise.gestion.rh.repository.EvaluationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;

    /**
     * Récupérer toutes les évaluations
     */
    public List<EvaluationDTO> getAllEvaluations() {
        return evaluationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer une évaluation par son ID
     */
    public EvaluationDTO getEvaluationById(Integer id) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Évaluation non trouvée avec l'ID: " + id));
        return convertToDTO(evaluation);
    }

    /**
     * Récupérer les évaluations par libellé (recherche partielle)
     */
    public List<EvaluationDTO> getEvaluationsByLibelle(String libelle) {
        return evaluationRepository.findByLibelleContainingIgnoreCase(libelle).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les évaluations avec un coefficient minimum
     */
    public List<EvaluationDTO> getEvaluationsByCoeffMin(Integer minCoeff) {
        return evaluationRepository.findByCoeffGreaterThanEqual(minCoeff).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Créer une nouvelle évaluation
     */
    public EvaluationDTO createEvaluation(EvaluationDTO evaluationDTO) {
        // Validation des données
        if (evaluationDTO.getLibelle() == null || evaluationDTO.getLibelle().trim().isEmpty()) {
            throw new RuntimeException("Le libellé de l'évaluation est obligatoire");
        }
        if (evaluationDTO.getCoeff() == null || evaluationDTO.getCoeff() <= 0) {
            throw new RuntimeException("Le coefficient doit être supérieur à 0");
        }

        Evaluation evaluation = convertToEntity(evaluationDTO);
        Evaluation savedEvaluation = evaluationRepository.save(evaluation);
        return convertToDTO(savedEvaluation);
    }

    /**
     * Mettre à jour une évaluation existante
     */
    public EvaluationDTO updateEvaluation(Integer id, EvaluationDTO evaluationDTO) {
        Evaluation existingEvaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Évaluation non trouvée avec l'ID: " + id));

        // Validation des données
        if (evaluationDTO.getLibelle() == null || evaluationDTO.getLibelle().trim().isEmpty()) {
            throw new RuntimeException("Le libellé de l'évaluation est obligatoire");
        }
        if (evaluationDTO.getCoeff() == null || evaluationDTO.getCoeff() <= 0) {
            throw new RuntimeException("Le coefficient doit être supérieur à 0");
        }

        // Mettre à jour les champs
        existingEvaluation.setLibelle(evaluationDTO.getLibelle());
        existingEvaluation.setCoeff(evaluationDTO.getCoeff());

        Evaluation updatedEvaluation = evaluationRepository.save(existingEvaluation);
        return convertToDTO(updatedEvaluation);
    }

    /**
     * Supprimer une évaluation
     */
    public void deleteEvaluation(Integer id) {
        if (!evaluationRepository.existsById(id)) {
            throw new RuntimeException("Évaluation non trouvée avec l'ID: " + id);
        }
        
        // Vérifier si l'évaluation est utilisée dans des notes
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Évaluation non trouvée avec l'ID: " + id));
        
        if (evaluation.getNotes() != null && !evaluation.getNotes().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer cette évaluation car elle est associée à des notes");
        }
        
        evaluationRepository.deleteById(id);
    }

    /**
     * Vérifier si une évaluation existe
     */
    public boolean existsEvaluation(Integer id) {
        return evaluationRepository.existsById(id);
    }

    /**
     * Méthode de conversion Entity -> DTO
     */
    private EvaluationDTO convertToDTO(Evaluation evaluation) {
        return new EvaluationDTO(
            evaluation.getId(),
            evaluation.getLibelle(),
            evaluation.getCoeff()
        );
    }

    /**
     * Méthode de conversion DTO -> Entity
     */
    private Evaluation convertToEntity(EvaluationDTO evaluationDTO) {
        Evaluation evaluation = new Evaluation();
        evaluation.setId(evaluationDTO.getId());
        evaluation.setLibelle(evaluationDTO.getLibelle());
        evaluation.setCoeff(evaluationDTO.getCoeff());
        return evaluation;
    }
}