package com.entreprise.gestion.rh.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BesoinCriteria {
    private Integer statut;           // ✅ Nouveau
    private Integer minAge;
    private Integer maxAge;
    private Integer minExperience;

    private Integer departementId;
    private Integer metierId;

    private List<Integer> competenceIds;
    private List<Integer> langueIds;
    private List<Integer> diplomeFiliereIds;
}
