package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Candidature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, Integer> {
<<<<<<< Updated upstream
}
=======
    boolean existsByCandidatIdAndBesoinId(Integer idCandidat, Integer idBesoin);

    List<Candidature> findByStatut(Integer statut);
    
    @Query("SELECT c FROM Candidature c WHERE c.besoin.id = :besoinId")
    List<Candidature> findByBesoinId(@Param("besoinId") Integer besoinId);
    
    @Query("SELECT c FROM Candidature c WHERE c.candidat.id = :candidatId")
    List<Candidature> findByCandidatId(@Param("candidatId") Integer candidatId);
    
    boolean existsById(Integer id);
}
>>>>>>> Stashed changes
