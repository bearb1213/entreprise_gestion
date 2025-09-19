package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.BesoinDiplomeFiliere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BesoinDiplomeFiliereRepository extends JpaRepository<BesoinDiplomeFiliere, Integer> {
}