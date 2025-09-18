package com.entreprise.gestion.rh.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "status_employe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusEmploye {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "libelle", length = 50)
    private String libelle;
    
    @Column(name = "date_entree")
    private LocalDateTime dateEntree;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employe_id", nullable = false)
    private Employe employe;
}