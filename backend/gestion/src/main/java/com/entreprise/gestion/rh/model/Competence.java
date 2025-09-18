package com.entreprise.gestion.rh.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.util.List;

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
    
    @ManyToMany(mappedBy = "competences", fetch = FetchType.LAZY)
    private List<Candidat> candidats;
    
    @OneToMany(mappedBy = "competence", fetch = FetchType.LAZY)
    private List<BesoinCompetence> besoinCompetences;
}
