package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Competence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetenceRepository extends JpaRepository<Competence, Integer> {
}