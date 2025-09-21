package com.entreprise.gestion.rh.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "diplome_filiere")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class DiplomeFiliere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "filiere_id")
    private Filiere filiere;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "diplome_id")
    private Diplome diplome;
    
    @OneToMany(mappedBy = "diplomeFiliere" , fetch = FetchType.LAZY)
    private List<BesoinDiplomeFiliere> besoinDiplomeFilieres;

    @ManyToMany(mappedBy = "diplomeFilieres") // doit correspondre au nom de la liste dans Candidat
    private List<Candidat> candidats;
}