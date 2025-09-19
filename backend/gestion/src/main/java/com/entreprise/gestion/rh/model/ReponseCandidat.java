package com.entreprise.gestion.rh.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "reponse_candidat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReponseCandidat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidature_id")
    private Candidature candidature;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "choix_id")
    private Choix choix;
}