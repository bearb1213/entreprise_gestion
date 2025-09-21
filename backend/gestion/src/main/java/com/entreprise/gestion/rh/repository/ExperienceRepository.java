package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Integer> {
    @Query("SELECT e FROM Experience e WHERE e.candidat.id = :candidatId")
    List<Experience> findByCandidatId(Integer candidatId);
}