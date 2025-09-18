package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {
}