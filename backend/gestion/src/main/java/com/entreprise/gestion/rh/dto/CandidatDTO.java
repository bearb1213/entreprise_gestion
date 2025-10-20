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
    
}