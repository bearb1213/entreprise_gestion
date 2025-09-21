package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.DiplomeFiliere;
import com.entreprise.gestion.rh.model.Filiere;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface DiplomeFiliereRepository extends JpaRepository<DiplomeFiliere, Integer> {
    List<DiplomeFiliere> findByFiliere(Filiere filiere);
    
    @Query("SELECT df FROM DiplomeFiliere df JOIN df.candidats cand WHERE cand.id = :candidatId")
    List<DiplomeFiliere> findByCandidatId(Integer candidatId);
}