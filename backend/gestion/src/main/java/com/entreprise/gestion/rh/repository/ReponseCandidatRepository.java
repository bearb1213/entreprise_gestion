package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.ReponseCandidat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReponseCandidatRepository extends JpaRepository<ReponseCandidat, Integer> {
    List<ReponseCandidat> findByCandidatureId(Integer candidatureId);
}