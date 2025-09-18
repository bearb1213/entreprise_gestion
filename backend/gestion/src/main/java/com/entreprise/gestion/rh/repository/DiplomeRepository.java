package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Diplome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiplomeRepository extends JpaRepository<Diplome, Integer> {
    
    // Trouver un diplôme par son libel
}