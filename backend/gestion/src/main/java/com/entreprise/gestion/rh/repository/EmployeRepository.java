package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Integer> {
}