package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Choix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChoixRepository extends JpaRepository<Choix, Integer> {
}