package com.entreprise.gestion.rh.dto;

import com.entreprise.gestion.rh.model.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CandidatDTO {
    private Integer id;
    private String description;
    private PersonneDTO personne;
    private List<CompetenceDTO> competences;
    private List<LangueDTO> langues;
    private List<DiplomeFiliereDTO> diplomes;
    private List<ExperienceDTO> experiences;
    
    @Data
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
    
    @Data
    public static class CompetenceDTO {
        private Integer id;
        private String libelle;
    }
    
    @Data
    public static class LangueDTO {
        private Integer id;
        private String libelle;
    }
    
    @Data
    public static class DiplomeFiliereDTO {
        private Integer id;
        private String libelle;
    }
    
    @Data
    public static class ExperienceDTO {
        private Integer id;
        private Integer nbAnnee;
        private MetierDTO metier;
    }
    
    @Data
    public static class MetierDTO {
        private Integer id;
        private String libelle;
    }
}