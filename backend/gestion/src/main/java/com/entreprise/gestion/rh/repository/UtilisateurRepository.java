package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
}