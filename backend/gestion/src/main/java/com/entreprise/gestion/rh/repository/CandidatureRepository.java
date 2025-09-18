package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Candidature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, Integer> {
}