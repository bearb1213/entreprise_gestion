package com.entreprise.gestion.rh.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "besoin_diplome_filiere")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BesoinDiplomeFiliere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private Integer coeff;
    
    @ManyToOne
    @JoinColumn(name = "besoin_id")
    private Besoin besoin;
    
    @ManyToOne
    @JoinColumn(name = "diplome_filiere_id")
    private DiplomeFiliere diplomeFiliere;
}