package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartementRepository extends JpaRepository<Departement, Integer> {
}