package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.model.Personne;
import com.entreprise.gestion.rh.repository.PersonneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonneService {

    private final PersonneRepository personneRepository;

    @Autowired
    public PersonneService(PersonneRepository personneRepository) {
        this.personneRepository = personneRepository;
    }

    /**
     * Crée une nouvelle personne
     * @param personne l'objet Personne à créer
     * @return la personne créée
     */
    public Personne createPersonne(Personne personne) {
        // Validation basique
        if (personne == null) {
            throw new IllegalArgumentException("La personne ne peut pas être null");
        }
        if (personne.getNom() == null || personne.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        if (personne.getPrenom() == null || personne.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom est obligatoire");
        }
        if (personne.getEmail() == null || personne.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'email est obligatoire");
        }

        // Vérifier si l'email existe déjà
        if (personneRepository.existsByEmail(personne.getEmail())) {
            throw new IllegalStateException("Une personne avec cet email existe déjà");
        }

        return personneRepository.save(personne);
    }

    /**
     * Récupère une personne par son ID
     * @param id l'ID de la personne
     * @return Optional contenant la personne si trouvée
     */
    public Optional<Personne> getPersonneById(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID invalide");
        }
        return personneRepository.findById(id);
    }

    /**
     * Récupère toutes les personnes
     * @return liste de toutes les personnes
     */
    public List<Personne> getAllPersonnes() {
        return personneRepository.findAll();
    }

    /**
     * Met à jour une personne existante
     * @param id l'ID de la personne à mettre à jour
     * @param personneDetails les nouvelles informations de la personne
     * @return la personne mise à jour
     */
    public Personne updatePersonne(Integer id, Personne personneDetails) {
        Personne personne = getPersonneById(id)
                .orElseThrow(() -> new RuntimeException("Personne non trouvée avec l'ID: " + id));

        // Vérifier si le nouvel email est déjà utilisé par une autre personne
        if (personneDetails.getEmail() != null && 
            !personneDetails.getEmail().equals(personne.getEmail()) &&
            personneRepository.existsByEmailAndIdNot(personneDetails.getEmail(), id)) {
            throw new IllegalStateException("L'email est déjà utilisé par une autre personne");
        }

        // Mettre à jour les champs
        if (personneDetails.getNom() != null) {
            personne.setNom(personneDetails.getNom());
        }
        if (personneDetails.getPrenom() != null) {
            personne.setPrenom(personneDetails.getPrenom());
        }
        if (personneDetails.getEmail() != null) {
            personne.setEmail(personneDetails.getEmail());
        }
        if (personneDetails.getDateNaissance() != null) {
            personne.setDateNaissance(personneDetails.getDateNaissance());
        }
        if (personneDetails.getGenre() != null) {
            personne.setGenre(personneDetails.getGenre());
        }
        if (personneDetails.getVille() != null) {
            personne.setVille(personneDetails.getVille());
        }
        if (personneDetails.getTelephone() != null) {
            personne.setTelephone(personneDetails.getTelephone());
        }
        if (personneDetails.getImage() != null) {
            personne.setImage(personneDetails.getImage());
        }

        return personneRepository.save(personne);
    }

    /**
     * Supprime une personne par son ID
     * @param id l'ID de la personne à supprimer
     */
    public void deletePersonne(Integer id) {
        if (!personneRepository.existsById(id)) {
            throw new RuntimeException("Personne non trouvée avec l'ID: " + id);
        }
        personneRepository.deleteById(id);
    }

    /**
     * Vérifie si une personne existe par son ID
     * @param id l'ID de la personne
     * @return true si la personne existe
     */
    public boolean personneExists(Integer id) {
        return personneRepository.existsById(id);
    }

    /**
     * Recherche des personnes par nom
     * @param nom le nom à rechercher
     * @return liste des personnes correspondantes
     */
    public List<Personne> findByNom(String nom) {
        return personneRepository.findByNomContainingIgnoreCase(nom);
    }

    /**
     * Recherche des personnes par email
     * @param email l'email à rechercher
     * @return Optional contenant la personne si trouvée
     */
    public Optional<Personne> findByEmail(String email) {
        return personneRepository.findByEmail(email);
    }

    /**
     * Recherche des personnes par ville
     * @param ville la ville à rechercher
     * @return liste des personnes de la ville
     */
    public List<Personne> findByVille(String ville) {
        return personneRepository.findByVille(ville);
    }
}