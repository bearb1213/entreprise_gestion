package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.BesoinCompetence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BesoinCompetenceRepository extends JpaRepository<BesoinCompetence, Integer> {
}