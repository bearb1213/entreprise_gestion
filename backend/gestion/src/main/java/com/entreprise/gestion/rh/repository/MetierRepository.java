package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Metier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetierRepository extends JpaRepository<Metier, Integer> {
}