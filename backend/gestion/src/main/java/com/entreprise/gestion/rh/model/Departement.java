package com.entreprise.gestion.rh.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Departement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "libelle", length = 50)
    private String libelle;
    
    @OneToMany(mappedBy = "departement", fetch = FetchType.LAZY)
    private List<Utilisateur> utilisateurs = new ArrayList<>();
    
    @OneToMany(mappedBy = "departement", fetch = FetchType.LAZY)
    private List<Besoin> besoins = new ArrayList<>();
    
    @OneToMany(mappedBy = "departement", fetch = FetchType.LAZY)
    private List<Question> questions = new ArrayList<>();
}