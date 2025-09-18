package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.DiplomeFiliere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiplomeFiliereRepository extends JpaRepository<DiplomeFiliere, Integer> {
}