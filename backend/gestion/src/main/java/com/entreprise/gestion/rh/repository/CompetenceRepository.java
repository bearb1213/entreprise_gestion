package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Competence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface CompetenceRepository extends JpaRepository<Competence, Integer> {
    @Query("SELECT c FROM Competence c JOIN c.candidats cand WHERE cand.id = :candidatId")
    List<Competence> findByCandidatId(Integer candidatId);
}