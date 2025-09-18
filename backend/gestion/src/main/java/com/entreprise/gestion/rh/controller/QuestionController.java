package com.entreprise.gestion.rh.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.entreprise.gestion.rh.dto.QuestionDto;
import com.entreprise.gestion.rh.model.Question;
import com.entreprise.gestion.rh.service.QuestionService;

@RestController
@RequestMapping("/questions")
public class QuestionController {
    
    @Autowired
    private QuestionService questionService;
    
    @PostMapping({"/", ""})
    public List<Object> getQuestions(@RequestParam("id_dept") Integer deptId, @RequestParam("id_metier") Integer metierId) {
        List<Object> response = new ArrayList<>();
        int nbDeptQuestions = 3;
        int nbMetierQuestions = 3;
        int nbGeneralQuestions = 4;

        List<Question> questionsDept = questionService.getQuestionsAleatoiresParDepartement(deptId, nbDeptQuestions);
        List<Question> questionsMetier = questionService.getQuestionsAleatoiresParMetier(metierId, nbMetierQuestions);
        List<Question> questionsGen = questionService.getQuestionsAleatoiresGenerales(nbGeneralQuestions);

        List<QuestionDto> questionsDeptDto = questionService.questionsListToDto(questionsDept);
        List<QuestionDto> questionsDtoMetier = questionService.questionsListToDto(questionsMetier);
        List<QuestionDto> questionsDtoGen = questionService.questionsListToDto(questionsGen);


        response.addAll(questionsDeptDto);
        response.addAll(questionsDtoMetier);
        response.addAll(questionsDtoGen);

        return response;
    }
}


