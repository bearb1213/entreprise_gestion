package com.entreprise.gestion.rh.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "choix")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Choix {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "reponse", length = 255)
    private String reponse;
    
    @Column(name = "coeff")
    private Integer coeff;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
    
    @OneToMany(mappedBy = "choix", fetch = FetchType.LAZY)
    private List<ReponseCandidat> reponseCandidats = new ArrayList<>();
}