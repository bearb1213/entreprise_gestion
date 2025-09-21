package com.entreprise.gestion.rh.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "experience")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "nb_annee")
    private Integer nbAnnee;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "metier_id")
    private Metier metier;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "candidat_id")
    private Candidat candidat;
}