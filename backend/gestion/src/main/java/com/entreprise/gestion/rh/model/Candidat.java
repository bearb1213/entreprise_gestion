package com.entreprise.gestion.rh.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "candidat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Candidat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personne_id", nullable = false)
    private Personne personne;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "candidat_competence",
        joinColumns = @JoinColumn(name = "candidat_id"),
        inverseJoinColumns = @JoinColumn(name = "competence_id")
    )
    private List<Competence> competences = new ArrayList<>();
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "candidat_langue",
        joinColumns = @JoinColumn(name = "candidat_id"),
        inverseJoinColumns = @JoinColumn(name = "langue_id")
    )
    private List<Langue> langues = new ArrayList<>();
    
    @OneToMany(mappedBy = "candidat", fetch = FetchType.LAZY)
    private List<CandidatDiplomeFiliere> candidatDiplomeFilieres = new ArrayList<>();
    
    @OneToMany(mappedBy = "candidat", fetch = FetchType.LAZY)
    private List<Candidature> candidatures = new ArrayList<>();
    
    @OneToMany(mappedBy = "candidat", fetch = FetchType.LAZY)
    private List<Experience> experiences = new ArrayList<>();
}