package com.entreprise.gestion.rh.dto;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private Integer id;
    
    private String intitule;
    
    private Integer idDepartement;

    private String libelleDepartement;

    private Integer idMetier;

    private String libelleMetier;

    private List<ChoixDto> choix;
    
}
