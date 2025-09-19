package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Langue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LangueRepository extends JpaRepository<Langue, Integer> {
}