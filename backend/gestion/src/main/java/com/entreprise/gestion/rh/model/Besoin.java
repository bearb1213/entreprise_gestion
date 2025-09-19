package com.entreprise.gestion.rh.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "besoin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Besoin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "statut")
    private Integer statut;
    
    @Column(name = "min_age")
    private Integer minAge;
    
    @Column(name = "nb_poste_dsipo")
    private Integer nbPosteDispo;
    
    @Column(name = "coeff_age")
    private Integer coeffAge;
    
    @Column(name = "coeff_experience")
    private Integer coeffExperience;
    
    @Column(name = "max_age")
    private Integer maxAge;
    
    @Column(name = "min_experience")
    private Integer minExperience;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metier_id")
    @JsonIgnore
    private Metier metier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departement_id", nullable = false)
    @JsonIgnore
    private Departement departement;

    @OneToMany(mappedBy = "besoin", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BesoinCompetence> besoinCompetences = new ArrayList<>();

    @OneToMany(mappedBy = "besoin", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BesoinLangue> besoinLangues = new ArrayList<>();

    @OneToMany(mappedBy = "besoin", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BesoinDiplomeFiliere> besoinDiplomeFilieres = new ArrayList<>();

    @OneToMany(mappedBy = "besoin", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Candidature> candidatures = new ArrayList<>();

}