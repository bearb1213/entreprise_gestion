package com.entreprise.gestion.rh.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personne_id", nullable = false)
    private Personne personne;
    
    @OneToMany(mappedBy = "employe", fetch = FetchType.LAZY)
    private List<StatusEmploye> statusEmployes = new ArrayList<>();
}