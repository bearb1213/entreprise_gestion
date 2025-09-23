package com.entreprise.gestion.rh.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;


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
    @JsonIgnore
    private List<Candidat> candidats;
    
    @JsonIgnore
    @OneToMany(mappedBy = "langue", fetch = FetchType.LAZY)
    private List<BesoinLangue> besoinLangues;
}