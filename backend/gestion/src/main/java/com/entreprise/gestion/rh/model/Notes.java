package com.entreprise.gestion.rh.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    // Correction: Utiliser columnDefinition au lieu de precision/scale pour les types flottants
    @Column(name = "note", columnDefinition = "NUMERIC(15,2)")
    private Double note;
    
    @Column(name = "date_entree")
    private LocalDateTime dateEntree;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "evaluation_id", nullable = false)
    private Evaluation evaluation;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "candidature_id", nullable = false)
    private Candidature candidature;
}