package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Candidat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface CandidatRepository extends JpaRepository<Candidat, Integer> {
    @Query("SELECT c FROM Candidat c WHERE c.personne.email = :email")
    Optional<Candidat> findByEmail(String email);
}