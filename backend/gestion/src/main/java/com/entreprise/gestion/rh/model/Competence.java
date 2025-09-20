package com.entreprise.gestion.rh.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "competence")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Competence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String libelle;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "candidat_competence",
        joinColumns = @JoinColumn(name = "competence_id"),
        inverseJoinColumns = @JoinColumn(name = "candidat_id")
    )
    @JsonIgnore // Ajoutez cette annotation
    private List<Candidat> candidats;
    
    @OneToMany(mappedBy = "competence", fetch = FetchType.LAZY)
    @JsonIgnore // Ajoutez cette annotation
    private List<BesoinCompetence> besoinCompetences;
}