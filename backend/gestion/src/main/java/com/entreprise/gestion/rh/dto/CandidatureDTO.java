package com.entreprise.gestion.rh.dto;

import com.entreprise.gestion.rh.model.Besoin;
import com.entreprise.gestion.rh.model.Candidat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidatureDTO {
    private Integer id;
    private Integer statut;
    private LocalDateTime dateCandidature;
    private Besoin besoin;
    private Candidat candidat;

    
    
 
}