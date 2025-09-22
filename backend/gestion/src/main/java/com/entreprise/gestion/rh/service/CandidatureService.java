package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.dto.CandidatureDTO;
import com.entreprise.gestion.rh.model.Candidature;
import com.entreprise.gestion.rh.repository.CandidatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidatureService {

    private final CandidatureRepository candidatureRepository;

    /**
     * Récupérer toutes les candidatures avec conversion en DTO
     */
    public List<CandidatureDTO> getAllCandidatures() {
        return candidatureRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer une candidature par son ID
     */
    public CandidatureDTO getCandidatureById(Integer id) {
        Candidature candidature = candidatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidature non trouvée avec l'ID: " + id));
        return convertToDTO(candidature);
    }

    /**
     * Récupérer les candidatures par statut
     */
    public List<CandidatureDTO> getCandidaturesByStatut(Integer statut) {
        return candidatureRepository.findByStatut(statut).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les candidatures par besoin
     */
    public List<CandidatureDTO> getCandidaturesByBesoin(Integer besoinId) {
        return candidatureRepository.findByBesoinId(besoinId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les candidatures par candidat
     */
    public List<CandidatureDTO> getCandidaturesByCandidat(Integer candidatId) {
        return candidatureRepository.findByCandidatId(candidatId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Créer une nouvelle candidature
     */
    public CandidatureDTO createCandidature(CandidatureDTO candidatureDTO) {
        Candidature candidature = convertToEntity(candidatureDTO);
        Candidature savedCandidature = candidatureRepository.save(candidature);
        return convertToDTO(savedCandidature);
    }

    /**
     * Mettre à jour une candidature existante
     */
    public CandidatureDTO updateCandidature(Integer id, CandidatureDTO candidatureDTO) {
        Candidature existingCandidature = candidatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidature non trouvée avec l'ID: " + id));

        // Mettre à jour les champs
        existingCandidature.setStatut(candidatureDTO.getStatut());
        existingCandidature.setDateCandidature(candidatureDTO.getDateCandidature());
        // Note: besoin et candidat ne sont généralement pas modifiés après création

        Candidature updatedCandidature = candidatureRepository.save(existingCandidature);
        return convertToDTO(updatedCandidature);
    }

    /**
     * Mettre à jour le statut d'une candidature
     */
    public CandidatureDTO updateStatutCandidature(Integer id, Integer nouveauStatut) {
        Candidature candidature = candidatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidature non trouvée avec l'ID: " + id));

        candidature.setStatut(nouveauStatut);
        Candidature updatedCandidature = candidatureRepository.save(candidature);
        return convertToDTO(updatedCandidature);
    }

    /**
     * Supprimer une candidature
     */
    public void deleteCandidature(Integer id) {
        if (!candidatureRepository.existsById(id)) {
            throw new RuntimeException("Candidature non trouvée avec l'ID: " + id);
        }
        candidatureRepository.deleteById(id);
    }

    /**
     * Vérifier si une candidature existe
     */
    public boolean existsCandidature(Integer id) {
        return candidatureRepository.existsById(id);
    }

    /**
     * Méthode de conversion Entity -> DTO
     */
    private CandidatureDTO convertToDTO(Candidature candidature) {
        return new CandidatureDTO(
            candidature.getId(),
            candidature.getStatut(),
            candidature.getDateCandidature(),
            candidature.getBesoin(),
            candidature.getCandidat()
        );
    }

    /**
     * Méthode de conversion DTO -> Entity
     */
    private Candidature convertToEntity(CandidatureDTO candidatureDTO) {
        Candidature candidature = new Candidature();
        candidature.setId(candidatureDTO.getId());
        candidature.setStatut(candidatureDTO.getStatut());
        candidature.setDateCandidature(candidatureDTO.getDateCandidature());
        candidature.setBesoin(candidatureDTO.getBesoin());
        candidature.setCandidat(candidatureDTO.getCandidat());
        return candidature;
    }
}