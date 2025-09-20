package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Besoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BesoinRepository extends JpaRepository<Besoin, Integer> {
    
    List<Besoin> findByStatut(Integer statut);
    List<Besoin> findByMetierId(Integer metierId);
    List<Besoin> findByDepartementId(Integer departementId);
    boolean existsById(Integer id);
    
    // Ajoutez d'autres méthodes de recherche selon vos besoins
}