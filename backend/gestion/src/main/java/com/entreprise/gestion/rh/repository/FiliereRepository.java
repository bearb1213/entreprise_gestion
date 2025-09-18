package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FiliereRepository extends JpaRepository<Filiere, Integer> {
}