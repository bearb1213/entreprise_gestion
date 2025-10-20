package com.entreprise.gestion.rh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDTO {
    private Integer id;
    private String libelle;
    private Integer coeff;
    
    
    
}