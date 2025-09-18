package com.entreprise.gestion.rh.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "personne")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Personne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "nom", length = 50)
    private String nom;
    
    @Column(name = "image", length = 50)
    private String image;
    
    @Column(name = "prenom", length = 50)
    private String prenom;
    
    @Column(name = "email", length = 50)
    private String email;
    
    @Column(name = "date_naissance")
    private LocalDate dateNaissance;
    
    @Column(name = "genre")
    private Integer genre;
    
    @Column(name = "ville", length = 50)
    private String ville;
    
    @Column(name = "telephone", length = 50)
    private String telephone;
    
    @OneToOne(mappedBy = "personne", fetch = FetchType.LAZY)
    private Candidat candidat;
    
    @OneToOne(mappedBy = "personne", fetch = FetchType.LAZY)
    private Employe employe;
}