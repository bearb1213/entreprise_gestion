package com.entreprise.gestion.rh.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "besoin_langue")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BesoinLangue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private Integer coeff;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "besoin_id")
    private Besoin besoin;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "langue_id")
    private Langue langue;
}