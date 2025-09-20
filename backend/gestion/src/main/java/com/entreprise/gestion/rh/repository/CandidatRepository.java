package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Candidat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidatRepository extends JpaRepository<Candidat, Integer> {
    
    // ✅ Trouver un candidat par email de la personne
    @Query("SELECT c FROM Candidat c WHERE c.personne.email = :email")
    Optional<Candidat> findByPersonneEmail(@Param("email") String email);
    
    // ✅ Trouver des candidats par nom ou prénom
    @Query("SELECT c FROM Candidat c WHERE LOWER(c.personne.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(c.personne.prenom) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Candidat> findByNomOrPrenomContaining(@Param("searchTerm") String searchTerm);
    
    // ✅ Trouver des candidats par ville
    @Query("SELECT c FROM Candidat c WHERE LOWER(c.personne.ville) LIKE LOWER(CONCAT('%', :ville, '%'))")
    List<Candidat> findByVille(@Param("ville") String ville);
    
    // ✅ Trouver des candidats par genre
    @Query("SELECT c FROM Candidat c WHERE c.personne.genre = :genre")
    List<Candidat> findByGenre(@Param("genre") Integer genre);
    
    // ✅ Charger un candidat avec TOUTES ses relations (pour l'évaluation)
    @Query("SELECT DISTINCT c FROM Candidat c " +
           "LEFT JOIN FETCH c.personne " +
           "LEFT JOIN FETCH c.competences cc " +
           "LEFT JOIN FETCH cc.competence " +
           "LEFT JOIN FETCH c.langues cl " +
           "LEFT JOIN FETCH cl.langue " +
           "LEFT JOIN FETCH c.diplomes cd " +
           "LEFT JOIN FETCH cd.diplomeFiliere " +
           "LEFT JOIN FETCH c.experiences e " +
           "LEFT JOIN FETCH e.metier " +
           "WHERE c.id = :id")
    Optional<Candidat> findByIdWithAllRelations(@Param("id") Integer id);
    
    // ✅ Charger tous les candidats avec leurs relations de base
    @Query("SELECT DISTINCT c FROM Candidat c " +
           "LEFT JOIN FETCH c.personne " +
           "LEFT JOIN FETCH c.competences " +
           "LEFT JOIN FETCH c.langues " +
           "LEFT JOIN FETCH c.experiences")
    List<Candidat> findAllWithRelations();
    
    // ✅ Compter le nombre de candidats par ville
    @Query("SELECT c.personne.ville, COUNT(c) FROM Candidat c GROUP BY c.personne.ville")
    List<Object[]> countByVille();
    
    // ✅ Trouver des candidats ayant une compétence spécifique
    @Query("SELECT DISTINCT c FROM Candidat c " +
           "JOIN c.competences cc " +
           "JOIN cc.competence comp " +
           "WHERE comp.id = :competenceId")
    List<Candidat> findByCompetenceId(@Param("competenceId") Integer competenceId);
    
    // ✅ Trouver des candidats ayant une langue spécifique
    @Query("SELECT DISTINCT c FROM Candidat c " +
           "JOIN c.langues cl " +
           "JOIN cl.langue l " +
           "WHERE l.id = :langueId")
    List<Candidat> findByLangueId(@Param("langueId") Integer langueId);
    
    // ✅ Trouver des candidats ayant un diplôme spécifique
    @Query("SELECT DISTINCT c FROM Candidat c " +
           "JOIN c.diplomes cd " +
           "JOIN cd.diplomeFiliere df " +
           "WHERE df.id = :diplomeFiliereId")
    List<Candidat> findByDiplomeFiliereId(@Param("diplomeFiliereId") Integer diplomeFiliereId);
    
    // ✅ Trouver des candidats avec une expérience minimum dans un métier
    @Query("SELECT c FROM Candidat c " +
           "JOIN c.experiences e " +
           "WHERE e.metier.id = :metierId AND e.nbAnnee >= :minExperience")
    List<Candidat> findByMetierAndMinExperience(@Param("metierId") Integer metierId, 
                                               @Param("minExperience") Integer minExperience);
    
    // ✅ Vérifier si un email existe déjà
    boolean existsByPersonneEmail(String email);
    
    // ✅ Trouver des candidats par tranche d'âge
    @Query("SELECT c FROM Candidat c WHERE FUNCTION('YEAR', CURRENT_DATE) - FUNCTION('YEAR', c.personne.dateNaissance) BETWEEN :minAge AND :maxAge")
    List<Candidat> findByAgeBetween(@Param("minAge") Integer minAge, 
                                   @Param("maxAge") Integer maxAge);
    
    // ✅ Recherche avancée avec multiple critères
    @Query("SELECT c FROM Candidat c WHERE " +
           "(:nom IS NULL OR LOWER(c.personne.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
           "(:ville IS NULL OR LOWER(c.personne.ville) LIKE LOWER(CONCAT('%', :ville, '%'))) AND " +
           "(:minAge IS NULL OR FUNCTION('YEAR', CURRENT_DATE) - FUNCTION('YEAR', c.personne.dateNaissance) >= :minAge) AND " +
           "(:maxAge IS NULL OR FUNCTION('YEAR', CURRENT_DATE) - FUNCTION('YEAR', c.personne.dateNaissance) <= :maxAge)")
    List<Candidat> searchCandidats(@Param("nom") String nom,
                                  @Param("ville") String ville,
                                  @Param("minAge") Integer minAge,
                                  @Param("maxAge") Integer maxAge);
}