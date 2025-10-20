package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Candidat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
<<<<<<< Updated upstream

@Repository
public interface CandidatRepository extends JpaRepository<Candidat, Integer> {
=======
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.query.Param;


@Repository
public interface CandidatRepository extends JpaRepository<Candidat, Integer> {
    @Query("SELECT c FROM Candidat c WHERE c.personne.email = :email")
    Optional<Candidat> findByEmail(String email);

    boolean existsByPersonneEmail(String email);

    // ✅ Trouver des candidats par tranche d'âge
>>>>>>> Stashed changes
}