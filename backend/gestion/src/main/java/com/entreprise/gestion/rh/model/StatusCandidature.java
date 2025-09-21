package com.entreprise.gestion.rh.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "status_candidature")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusCandidature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "date_entree")
    private LocalDateTime dateEntree;
    
    @Column(name = "libelle", length = 50)
    private String libelle;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "candidature_id", nullable = false)
    private Candidature candidature;
}