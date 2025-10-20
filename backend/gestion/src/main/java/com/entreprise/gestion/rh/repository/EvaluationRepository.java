package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {
    
    List<Evaluation> findByLibelleContainingIgnoreCase(String libelle);
    
    @Query("SELECT e FROM Evaluation e WHERE e.coeff >= :minCoeff ORDER BY e.coeff DESC")
    List<Evaluation> findByCoeffGreaterThanEqual(@Param("minCoeff") Integer minCoeff);
    
    boolean existsById(Integer id);
}