package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface EmployeRepository extends JpaRepository<Employe, Integer> {
    
    // Vérifier si une personne est déjà employée
    boolean existsByPersonneId(Integer personneId);
    
    // Trouver un employé par l'ID de la personne
    Optional<Employe> findByPersonneId(Integer personneId);
    
    // Autres méthodes si nécessaire...
}