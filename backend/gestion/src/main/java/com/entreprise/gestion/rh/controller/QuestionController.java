package com.entreprise.gestion.rh.controller;


import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/submit/{id_candidature}")
    public HashMap<String,Object> submitQuestions(@RequestBody List<Map<String, Object>> reponses,@PathVariable("id_candidature") Integer idCandidature)
    {
        HashMap<String,Object> response = new HashMap<>();
        List<Float> notes = new ArrayList<>();
        try {
            for(Map<String, Object> rep : reponses){
                Integer idQuestion = (Integer) rep.get("questionId");
                List<Map<String, Object>> selections = (List<Map<String, Object>>) rep.get("responses");
                List<Integer> idSelections = new ArrayList<>();
                if(rep!=null && rep.size()>0)
                {
                    for(Map<String,Object> selection : selections)
                    {
                        
                        idSelections.add((Integer) selection.get("optionId"));
                    }
                    Float note = questionService.evaluateReponses(idCandidature, idQuestion, idSelections);
                    System.out.println("Question["+idQuestion+"]:"+note+"/1");
                    notes.add(note);
                }
            }

            Float noteFinale = questionService.evaluateQuestionnaire(idCandidature, notes);

            System.out.println("NOTE FINALE :"+noteFinale);

            response.put("status", "ok");
            response.put("message", "Reponses enregistrees avec succes");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", "Probleme survenu lors de l'evaluation du quiz");
            // TODO: handle exception
        }
        // request.getParameterMap().forEach((key,values)->{
        //     System.out.println("Question ID: " + key + ", Reponse: " + Arrays.toString(values));
        // });


        //normalement zany za eto manao traitement sy enregistrement anle reponses tsirairay fa aleo atao hoe nety par defaut fotsiny
        // - IMPORTANT: fonction a refactoriser
        return response;
    }
}


