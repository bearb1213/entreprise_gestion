package com.entreprise.gestion.rh.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "intitule", length = 255)
    private String intitule;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departement_id", nullable = false)
    private Departement departement;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metier_id")
    private Metier metier;
    
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Choix> choix = new ArrayList<>();
}