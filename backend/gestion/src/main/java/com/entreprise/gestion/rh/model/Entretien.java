package com.entreprise.gestion.rh.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "entretien")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entretien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "date_heure_debut")
    private LocalDateTime dateHeureDebut;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "candidature_id")
    private Candidature candidature;
}