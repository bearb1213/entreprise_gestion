package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Langue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface LangueRepository extends JpaRepository<Langue, Integer> {
    @Query("SELECT l FROM Langue l JOIN l.candidats cand WHERE cand.id = :candidatId")
    List<Langue> findByCandidatId(Integer candidatId);
}