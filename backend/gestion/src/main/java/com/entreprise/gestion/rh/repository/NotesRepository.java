package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Integer> {
    
    @Query("SELECT COUNT(n) > 0 FROM Notes n WHERE n.candidature.id = :candidatureId AND n.evaluation.id = :evaluationId")
    boolean existsByCandidatureIdAndEvaluationId(@Param("candidatureId") Integer candidatureId, 
                                                @Param("evaluationId") Integer evaluationId);
}