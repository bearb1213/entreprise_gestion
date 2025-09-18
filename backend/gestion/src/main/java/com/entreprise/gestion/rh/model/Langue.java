package com.entreprise.gestion.rh.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "langue")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Langue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String libelle;
    
    @ManyToMany(mappedBy = "langues" , fetch = FetchType.LAZY)
    private List<Candidat> candidats;
    
    @OneToMany(mappedBy = "langue", fetch = FetchType.LAZY)
    private List<BesoinLangue> besoinLangues;
}