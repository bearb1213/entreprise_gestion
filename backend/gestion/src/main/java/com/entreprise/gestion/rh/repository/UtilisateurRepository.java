package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
<<<<<<< Updated upstream

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
=======
import java.util.Optional;
import java.util.List;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    Optional<Utilisateur> findByLogin(String login);

    List<Utilisateur> findByDepartementLibelle(String libelle);
>>>>>>> Stashed changes
}