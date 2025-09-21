package com.entreprise.gestion.rh.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "candidature")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder  
public class Candidature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "statut")
    private Integer statut;
    
    @Column(name = "date_candidature")
    private LocalDateTime dateCandidature;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "besoin_id", nullable = false)
    private Besoin besoin;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "candidat_id", nullable = false)
    private Candidat candidat;
    
    @OneToMany(mappedBy = "candidature", fetch = FetchType.LAZY)
    private List<ReponseCandidat> reponseCandidats = new ArrayList<>();
    
    @OneToMany(mappedBy = "candidature", fetch = FetchType.LAZY)
    private List<Entretien> entretiens = new ArrayList<>();
    
    @OneToMany(mappedBy = "candidature", fetch = FetchType.LAZY)
    private List<Notes> notes = new ArrayList<>();
    
    @OneToMany(mappedBy = "candidature", fetch = FetchType.LAZY)
    private List<StatusCandidature> statusCandidatures = new ArrayList<>();
}