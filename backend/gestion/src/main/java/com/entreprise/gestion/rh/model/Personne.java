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
    
    // CORRECTION 1: Ajout de cascade
    @OneToOne(mappedBy = "personne", fetch = FetchType.LAZY, 
              cascade = CascadeType.ALL, orphanRemoval = true)
    private Candidat candidat;
    
    // CORRECTION 2: Ajout de cascade
    @OneToOne(mappedBy = "personne", fetch = FetchType.LAZY, 
              cascade = CascadeType.ALL, orphanRemoval = true)
    private Employe employe;
    
    // CORRECTION 3: Méthodes helpers pour gérer les relations bidirectionnelles
    public void setCandidat(Candidat candidat) {
        if (candidat == null) {
            if (this.candidat != null) {
                this.candidat.setPersonne(null);
            }
        } else {
            candidat.setPersonne(this);
        }
        this.candidat = candidat;
    }
    
    public void setEmploye(Employe employe) {
        if (employe == null) {
            if (this.employe != null) {
                this.employe.setPersonne(null);
            }
        } else {
            employe.setPersonne(this);
        }
        this.employe = employe;
    }
}