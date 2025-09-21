package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.DiplomeFiliere;
import com.entreprise.gestion.rh.model.Filiere;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DiplomeFiliereRepository extends JpaRepository<DiplomeFiliere, Integer> {
    List<DiplomeFiliere> findByFiliere(Filiere filiere);
}