package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Entretien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;


@Repository
public interface EntretienRepository extends JpaRepository<Entretien, Integer> {
    List<Entretien> findByRhIdAndDateHeureDebutAfter(Integer rhId, LocalDateTime dateHeureDebut);
    List<Entretien> findByRhIdAndDateHeureDebutBetween(Integer rhId, LocalDateTime start, LocalDateTime end);
}