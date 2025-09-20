package com.entreprise.gestion.rh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidatDTO {
    private Integer id;
    private String description;
    private PersonneDTO personne;
    
    // ✅ Changement : Utiliser des IDs au lieu des objets complets
    private List<Integer> competencesIds;
    private List<Integer> languesIds;
    private List<Integer> diplomesIds;
    private List<ExperienceDTO> experiences;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonneDTO {
        private Integer id;
        private String nom;
        private String prenom;
        private String email;
        private LocalDate dateNaissance;
        private Integer genre;
        private String ville;
        private String telephone;
    }
    
    // ✅ Garder les DTOs pour d'autres usages (affichage, etc.)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompetenceDTO {
        private Integer id;
        private String libelle;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LangueDTO {
        private Integer id;
        private String libelle;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiplomeFiliereDTO {
        private Integer id;
        private String libelle;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExperienceDTO {
        private Integer id;
        private Integer nbAnnee;
        private Integer metierId; // ✅ Changement : utiliser ID au lieu de l'objet complet
        private String metierLibelle; // ✅ Optionnel : pour l'affichage
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetierDTO {
        private Integer id;
        private String libelle;
    }
    
    // ✅ Méthodes utilitaires pour la conversion
    public static CandidatDTO fromEntity(com.entreprise.gestion.rh.model.Candidat candidat) {
        CandidatDTO dto = new CandidatDTO();
        dto.setId(candidat.getId());
        dto.setDescription(candidat.getDescription());
        
        if (candidat.getPersonne() != null) {
            PersonneDTO personneDTO = new PersonneDTO();
            personneDTO.setId(candidat.getPersonne().getId());
            personneDTO.setNom(candidat.getPersonne().getNom());
            personneDTO.setPrenom(candidat.getPersonne().getPrenom());
            personneDTO.setEmail(candidat.getPersonne().getEmail());
            personneDTO.setDateNaissance(candidat.getPersonne().getDateNaissance());
            personneDTO.setGenre(candidat.getPersonne().getGenre());
            personneDTO.setVille(candidat.getPersonne().getVille());
            personneDTO.setTelephone(candidat.getPersonne().getTelephone());
            dto.setPersonne(personneDTO);
        }
        
        // Extraire les IDs des compétences
        if (candidat.getCompetences() != null) {
            List<Integer> competenceIds = candidat.getCompetences().stream()
                .map(cc -> cc.getCompetence().getId())
                .collect(java.util.stream.Collectors.toList());
            dto.setCompetencesIds(competenceIds);
        }
        
        // Extraire les IDs des langues
        if (candidat.getLangues() != null) {
            List<Integer> langueIds = candidat.getLangues().stream()
                .map(cl -> cl.getLangue().getId())
                .collect(java.util.stream.Collectors.toList());
            dto.setLanguesIds(langueIds);
        }
        
        // Extraire les IDs des diplômes
        if (candidat.getDiplomes() != null) {
            List<Integer> diplomeIds = candidat.getDiplomes().stream()
                .map(cdf -> cdf.getDiplomeFiliere().getId())
                .collect(java.util.stream.Collectors.toList());
            dto.setDiplomesIds(diplomeIds);
        }
        
        // Convertir les expériences
        if (candidat.getExperiences() != null) {
            List<ExperienceDTO> experienceDTOs = candidat.getExperiences().stream()
                .map(experience -> {
                    ExperienceDTO expDTO = new ExperienceDTO();
                    expDTO.setId(experience.getId());
                    expDTO.setNbAnnee(experience.getNbAnnee());
                    if (experience.getMetier() != null) {
                        expDTO.setMetierId(experience.getMetier().getId());
                        expDTO.setMetierLibelle(experience.getMetier().getLibelle());
                    }
                    return expDTO;
                })
                .collect(java.util.stream.Collectors.toList());
            dto.setExperiences(experienceDTOs);
        }
        
        return dto;
    }
}