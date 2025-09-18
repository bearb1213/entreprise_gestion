package com.entreprise.gestion.rh.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entreprise.gestion.rh.dto.ChoixDto;
import com.entreprise.gestion.rh.dto.QuestionDto;
import com.entreprise.gestion.rh.model.Choix;
import com.entreprise.gestion.rh.model.Question;
import com.entreprise.gestion.rh.repository.QuestionRepository;


@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getQuestionsAleatoiresParDepartement(Integer id,int nb)
    {
        
        List<Question> questions = questionRepository.findByDepartementId(id);
        Collections.shuffle(questions);

        return questions.stream()
                    .limit(nb)
                    .collect(Collectors.toList());
    }

    public List<Question> getQuestionsAleatoiresParMetier(Integer id,int nb)
    {
        
        List<Question> questions = questionRepository.findByMetierId(id);
        Collections.shuffle(questions);

        return questions.stream()
                    .limit(nb)
                    .collect(Collectors.toList());
    }

    public List<Question> getQuestionsAleatoiresGenerales(int nb)
    {
        
        List<Question> questions = questionRepository.findQuestionsGenerales();
        Collections.shuffle(questions);

        return questions.stream()
                    .limit(nb)
                    .collect(Collectors.toList());
    }

    public List<QuestionDto> questionsListToDto(List<Question> questions) {
    if (questions == null) {
        return Collections.emptyList();
    }
    
    return questions.stream()
            .map(this::questionToDto)
            .collect(Collectors.toList());
}

private QuestionDto questionToDto(Question question) {
    if (question == null) {
        return null;
    }
    
    QuestionDto dto = new QuestionDto();
    dto.setId(question.getId());
    dto.setIntitule(question.getIntitule());
    
    // Gestion du département
    if (question.getDepartement() != null) {
        dto.setIdDepartement(question.getDepartement().getId());
        dto.setLibelleDepartement(question.getDepartement().getLibelle());
    }
    
    // Gestion du métier
    if (question.getMetier() != null) {
        dto.setIdMetier(question.getMetier().getId());
        dto.setLibelleMetier(question.getMetier().getLibelle());
    }
    
    // Conversion des choix
    if (question.getChoix() != null && !question.getChoix().isEmpty()) {
        List<ChoixDto> choixDtos = question.getChoix().stream()
                .map(choix -> {
                    ChoixDto choixDto = new ChoixDto();
                    choixDto.setId(choix.getId());
                    choixDto.setReponse(choix.getReponse());
                    choixDto.setCoeff(choix.getCoeff());
                    return choixDto;
                })
                .collect(Collectors.toList());
        dto.setChoix(choixDtos);
    } else {
        dto.setChoix(Collections.emptyList());
    }
    
    return dto;
}



}
