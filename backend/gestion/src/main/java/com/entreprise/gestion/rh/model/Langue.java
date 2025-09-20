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
    
    // CORRECTION : Supprimez @ManyToMany avec mappedBy
    // @ManyToMany(mappedBy = "langues", fetch = FetchType.LAZY)
    // private List<Candidat> candidats;
    
    // OPTION 1 : Utilisez @ManyToMany avec @JoinTable
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "candidat_langue",
        joinColumns = @JoinColumn(name = "langue_id"),
        inverseJoinColumns = @JoinColumn(name = "candidat_id")
    )
    private List<Candidat> candidats;
    
    // OPTION 2 : Ou utilisez la relation via l'entité de jointure
    // @OneToMany(mappedBy = "langue", fetch = FetchType.LAZY)
    // private List<CandidatLangue> candidatLangues;
    
    @OneToMany(mappedBy = "langue", fetch = FetchType.LAZY)
    private List<BesoinLangue> besoinLangues;
}