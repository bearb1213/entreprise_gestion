package com.entreprise.gestion.rh.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "diplome")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diplome {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String libelle;
    
    @OneToMany(mappedBy = "diplome" , fetch = FetchType.LAZY)
    private List<DiplomeFiliere> diplomeFilieres;
}