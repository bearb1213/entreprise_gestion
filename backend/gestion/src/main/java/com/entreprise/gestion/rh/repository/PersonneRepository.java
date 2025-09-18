package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonneRepository extends JpaRepository<Personne, Integer> {
    
    // Trouver une personne par email
    Optional<Personne> findByEmail(String email);
    
    // Vérifier si un email existe
    boolean existsByEmail(String email);
    
    // Vérifier si un email existe pour une autre personne
    boolean existsByEmailAndIdNot(String email, Integer id);
    
    // Rechercher par nom (contient, insensible à la casse)
    List<Personne> findByNomContainingIgnoreCase(String nom);
    
    // Rechercher par prénom (contient, insensible à la casse)
    List<Personne> findByPrenomContainingIgnoreCase(String prenom);
    
    // Rechercher par ville
    List<Personne> findByVille(String ville);
    
    // Recherche avancée par nom et prénom
    List<Personne> findByNomAndPrenom(String nom, String prenom);
    
    // Recherche personnalisée avec JPQL
    @Query("SELECT p FROM Personne p WHERE LOWER(p.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.prenom) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Personne> searchByName(@Param("searchTerm") String searchTerm);
    
}