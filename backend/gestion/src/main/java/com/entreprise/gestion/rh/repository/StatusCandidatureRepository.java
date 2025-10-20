package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.StatusCandidature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StatusCandidatureRepository extends JpaRepository<StatusCandidature, Integer> {
    
    List<StatusCandidature> findByCandidatureId(Integer candidatureId);
    
    List<StatusCandidature> findByEvaluationId(Integer evaluationId);
    
    List<StatusCandidature> findByCandidatureIdOrderByDateEntreeDesc(Integer candidatureId);
}