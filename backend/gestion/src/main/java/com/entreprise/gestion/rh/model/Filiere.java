package com.entreprise.gestion.rh.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "filiere")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filiere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String libelle;
    
    @OneToMany(mappedBy = "filiere", fetch = FetchType.LAZY)
     @JsonIgnore
    private List<DiplomeFiliere> diplomeFilieres;
}