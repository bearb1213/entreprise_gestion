package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Integer> {
}