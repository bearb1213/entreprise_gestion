package com.entreprise.gestion.rh.repository;

import com.entreprise.gestion.rh.model.Question;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query("SELECT q FROM Question q JOIN FETCH q.choix")
    List<Question> findAllQuestionsWithChoix();

    @Query("SELECT q FROM Question q JOIN FETCH q.choix WHERE q.departement.id = :id")
    List<Question> findByDepartementId(@Param("id") Integer id);

    @Query("SELECT q FROM Question q JOIN FETCH q.choix WHERE q.metier.id = :id")
    List<Question> findByMetierId(@Param("id") Integer id);

    @Query("SELECT q FROM Question q JOIN FETCH q.choix WHERE q.departement IS NULL AND q.metier IS NULL")
    List<Question> findQuestionsGenerales();
}