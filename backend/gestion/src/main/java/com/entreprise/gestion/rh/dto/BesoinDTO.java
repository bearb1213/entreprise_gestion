package com.entreprise.gestion.rh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BesoinDTO {
    private Integer id;
    private Integer statut;
    private Integer minAge;
    private Integer nbPosteDsipo;
    private Integer coeffAge;
    private Integer coeffExperience;
    private Integer maxAge;
    private Integer minExperience;
    private MetierDTO metier;
    private DepartementDTO departement;
    private List<BesoinCompetenceDTO> besoinCompetences;
    private List<BesoinLangueDTO> besoinLangues;
    private List<BesoinDiplomeFiliereDTO> besoinDiplomeFilieres;
    
    // DTOs pour les entités liées
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetierDTO {
        private Integer id;
        private String libelle;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartementDTO {
        private Integer id;
        private String libelle;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BesoinCompetenceDTO {
        private Integer id;
        private Integer coeff;
        private CompetenceDTO competence;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BesoinLangueDTO {
        private Integer id;
        private Integer coeff;
        private LangueDTO langue;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BesoinDiplomeFiliereDTO {
        private Integer id;
        private Integer coeff;
        private DiplomeFiliereDTO diplomeFiliere;
    }
    
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
        private DiplomeDTO diplome;
        private FiliereDTO filiere;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiplomeDTO {
        private Integer id;
        private String libelle;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FiliereDTO {
        private Integer id;
        private String libelle;
    }
}