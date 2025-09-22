package com.entreprise.gestion.rh.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BesoinRequest {
    private Integer statut;
    private Integer minAge;
    private Integer maxAge;
    private Integer nbPosteDispo;
    private Integer minExperience;
    private Integer coeffAge;
    private Integer coeffExperience;
    
    private Integer metierId;
    private Integer departementId;
    
    private List<IdWithCoeff> competences;       
    private List<IdWithCoeff> langues;           
    private List<IdWithCoeff> diplomeFilieres;
}

