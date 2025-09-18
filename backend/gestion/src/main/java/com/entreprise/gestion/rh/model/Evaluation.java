package com.entreprise.gestion.rh.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "evaluation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "libelle", length = 50)
    private String libelle;
    
    @Column(name = "coeff")
    private Integer coeff;
    
    @OneToMany(mappedBy = "evaluation", fetch = FetchType.LAZY)
    private List<Notes> notes = new ArrayList<>();
}