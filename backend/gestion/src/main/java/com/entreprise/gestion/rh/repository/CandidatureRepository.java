package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Candidature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, Integer> {
    
    List<Candidature> findByStatut(Integer statut);
    
    @Query("SELECT c FROM Candidature c WHERE c.besoin.id = :besoinId")
    List<Candidature> findByBesoinId(@Param("besoinId") Integer besoinId);
    
    @Query("SELECT c FROM Candidature c WHERE c.candidat.id = :candidatId")
    List<Candidature> findByCandidatId(@Param("candidatId") Integer candidatId);
    
    boolean existsById(Integer id);
}