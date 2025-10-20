package com.entreprise.gestion.rh.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BesoinDTO {
    private Integer id;
    private Integer statut;
    private Integer minAge;
    private Integer nbPosteDispo;
    private Integer coeffAge;
    private Integer coeffExperience;
    private Integer maxAge;
    private Integer minExperience;
    private MetierDTO metier;
    private DepartementDTO departement;

    // Format attendu par le backend
    private List<BesoinCompetenceDTO> besoinCompetences;
    private List<BesoinLangueDTO> besoinLangues;
    private List<BesoinDiplomeFiliereDTO> besoinDiplomeFilieres;
    
    // Format alternatif envoyé par le frontend
    @JsonProperty("competences")
    private List<CompetenceInputDTO> competences;

    @JsonProperty("langues")
    private List<LangueInputDTO> langues;

    @JsonProperty("diplomeFilieres")
    private List<Object> diplomeFilieres; // Peut être Integer ou objet

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

    // Nouveaux DTOs pour le format frontend
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompetenceInputDTO {
        private Integer id;
        private Integer coeff;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LangueInputDTO {
        private Integer id;
        private Integer coeff;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiplomeFiliereInputDTO {
        private Integer id;
        private Integer coeff;
    }
}