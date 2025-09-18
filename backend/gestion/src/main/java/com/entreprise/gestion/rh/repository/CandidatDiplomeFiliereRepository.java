package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.CandidatDiplomeFiliere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidatDiplomeFiliereRepository extends JpaRepository<CandidatDiplomeFiliere, Integer> {
}