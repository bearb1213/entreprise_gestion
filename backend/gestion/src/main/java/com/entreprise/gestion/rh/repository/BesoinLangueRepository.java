package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.BesoinLangue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BesoinLangueRepository extends JpaRepository<BesoinLangue, Integer> {
}