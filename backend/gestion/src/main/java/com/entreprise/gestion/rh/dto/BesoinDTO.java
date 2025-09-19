package com.entreprise.gestion.rh.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BesoinDTO {
    private Integer id;
    private Integer statut;
    private Integer minAge;
    private Integer maxAge;
    private Integer nbPosteDispo;
    private Integer minExperience;
    private Integer coeffAge;
    private Integer coeffExperience;

    private String metierNom;
    private String departementNom;
    private List<String> competences;
    private List<String> langues;
    private List<String> diplomes;
}
