package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Besoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BesoinRepository extends JpaRepository<Besoin, Integer> {
}