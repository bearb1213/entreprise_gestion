package com.entreprise.gestion.rh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CandidatureSimpleDTO {
    private Integer id;
    private Integer statut;
    private LocalDateTime dateCandidature;
    private String besoinTitre;
    private String candidatNom;
    private String poste; // Nouveau champ ajouté
    
    // Constructeur pour faciliter la conversion
    public CandidatureSimpleDTO(Integer id, Integer statut, LocalDateTime dateCandidature, 
                               String besoinTitre, String candidatNom, String poste) {
        this.id = id;
        this.statut = statut;
        this.dateCandidature = dateCandidature;
        this.besoinTitre = besoinTitre;
        this.candidatNom = candidatNom;
        this.poste = poste;
    }
}