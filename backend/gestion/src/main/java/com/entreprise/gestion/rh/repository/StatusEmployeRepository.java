package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.StatusEmploye;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusEmployeRepository extends JpaRepository<StatusEmploye, Integer> {
}