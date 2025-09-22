package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.exception.MyException;
import com.entreprise.gestion.rh.dto.CandidatureSimpleDTO;
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

    public Candidature findCandidatureById(Integer id) throws Exception
    {
        return candidatureRepository.findById(id).orElseThrow(()->new MyException("Candidature introuvable"));
    }
  
    /**
     * Récupérer toutes les candidatures avec conversion en DTO simplifié
     */
    public List<CandidatureSimpleDTO> getAllCandidatures() {
        return candidatureRepository.findAll().stream()
                .map(this::convertToSimpleDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer une candidature par son ID en DTO simplifié
     */
    public CandidatureSimpleDTO getCandidatureById(Integer id) {
        Candidature candidature = candidatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidature non trouvée avec l'ID: " + id));
        return convertToSimpleDTO(candidature);
    }

    /**
     * Récupérer les candidatures par statut en DTO simplifié
     */
    public List<CandidatureSimpleDTO> getCandidaturesByStatut(Integer statut) {
        return candidatureRepository.findByStatut(statut).stream()
                .map(this::convertToSimpleDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les candidatures par besoin en DTO simplifié
     */
    public List<CandidatureSimpleDTO> getCandidaturesByBesoin(Integer besoinId) {
        return candidatureRepository.findByBesoinId(besoinId).stream()
                .map(this::convertToSimpleDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les candidatures par candidat en DTO simplifié
     */
    public List<CandidatureSimpleDTO> getCandidaturesByCandidat(Integer candidatId) {
        return candidatureRepository.findByCandidatId(candidatId).stream()
                .map(this::convertToSimpleDTO)
                .collect(Collectors.toList());
    }

    /**
     * Créer une nouvelle candidature - DÉSACTIVÉ car nécessite un DTO complet
     */
    public CandidatureSimpleDTO createCandidature(CandidatureSimpleDTO candidatureDTO) {
        throw new UnsupportedOperationException("Utilisez un DTO complet avec les objets Besoin et Candidat pour la création");
    }

    /**
     * Mettre à jour une candidature existante - DÉSACTIVÉ car nécessite un DTO complet
     */
    public CandidatureSimpleDTO updateCandidature(Integer id, CandidatureSimpleDTO candidatureDTO) {
        throw new UnsupportedOperationException("Utilisez un DTO complet avec les objets Besoin et Candidat pour la mise à jour");
    }

    /**
     * Mettre à jour le statut d'une candidature
     */
    public CandidatureSimpleDTO updateStatutCandidature(Integer id, Integer nouveauStatut) {
        Candidature candidature = candidatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidature non trouvée avec l'ID: " + id));

        candidature.setStatut(nouveauStatut);
        Candidature updatedCandidature = candidatureRepository.save(candidature);
        return convertToSimpleDTO(updatedCandidature);
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
     * Méthode de conversion Entity -> DTO simplifié
     */
    private CandidatureSimpleDTO convertToSimpleDTO(Candidature candidature) {
        return new CandidatureSimpleDTO(
            candidature.getId(),
            candidature.getStatut(),
            candidature.getDateCandidature(),
            // Récupérer seulement le titre du besoin
            candidature.getBesoin() != null ? candidature.getBesoin().toString() : "N/A",
            // Récupérer seulement le nom du candidat
            candidature.getCandidat() != null && candidature.getCandidat().getPersonne() != null ?
                candidature.getCandidat().getPersonne().getPrenom() + " " + 
                candidature.getCandidat().getPersonne().getNom() : "N/A"
        );
    }

    // NOTE: Les méthodes de conversion DTO -> Entity sont supprimées car
    // le DTO simplifié ne contient pas les informations nécessaires
    // pour recréer les objets Besoin et Candidat

    /**
     * Méthode alternative pour créer une candidature à partir d'IDs
     * (nécessite un service pour trouver les entités par ID)
     */
    public CandidatureSimpleDTO createCandidatureFromIds(Integer besoinId, Integer candidatId, Integer statut) {
        // Implémentation nécessitant des services supplémentaires
        // pour trouver le Besoin et le Candidat par leurs IDs
        throw new UnsupportedOperationException("Méthode non implémentée - nécessite des services supplémentaires");
    }
}
