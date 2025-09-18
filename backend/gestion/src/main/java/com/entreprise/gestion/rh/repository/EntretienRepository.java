package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Entretien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntretienRepository extends JpaRepository<Entretien, Integer> {
}