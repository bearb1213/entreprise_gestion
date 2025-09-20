package com.entreprise.gestion.rh.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entreprise.gestion.exception.MyException;
import com.entreprise.gestion.rh.dto.ChoixDto;
import com.entreprise.gestion.rh.dto.QuestionDto;
import com.entreprise.gestion.rh.model.Candidature;
import com.entreprise.gestion.rh.model.Choix;
import com.entreprise.gestion.rh.model.Entretien;
import com.entreprise.gestion.rh.model.Notes;
import com.entreprise.gestion.rh.model.Question;
import com.entreprise.gestion.rh.model.ReponseCandidat;
import com.entreprise.gestion.rh.repository.CandidatureRepository;
import com.entreprise.gestion.rh.repository.ChoixRepository;
import com.entreprise.gestion.rh.repository.EvaluationRepository;
import com.entreprise.gestion.rh.repository.NotesRepository;
import com.entreprise.gestion.rh.repository.QuestionRepository;

import jakarta.persistence.EntityNotFoundException;


@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired 
    private CandidatureRepository candidatureRepository;

    @Autowired 
    private ChoixRepository choixRepository;

    @Autowired 
    private NotesRepository notesRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    //-- dependances de services
    @Autowired
    private ChoixService choixService;

    @Autowired
    private EntretienService entretienService;

    @Autowired 
    private ReponseCandidatService reponseCandidatService;

    public Question findQuestionById(Integer id)
    {
        return questionRepository.findById(id).orElseThrow();
    }

    public Question saveQuestion(Question question)
    {
        return questionRepository.save(question);
    }

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

public Float evaluateReponses(Integer idCandidature, Integer idQuestion, List<Integer> choix) throws Exception
{
    // Vérifier d'abord l'existence de la candidature
    Candidature candidature = candidatureRepository.findById(idCandidature)
        .orElseThrow(() -> new MyException("Candidature non trouvée avec l'ID: " + idCandidature));
    
    Question questionActuelle = this.findQuestionById(idQuestion);
    
    if(questionActuelle.getChoix().size() == choix.size()) {
        return 0f;
    }
    if(choix == null || choix.size()<=0)
    {
        return 0f;
    }


    float valeurMax = 0;
    float noteObtenue = 0;

    for (Integer idChoix : choix) {
        Choix choixObj = choixService.findChoixById(idChoix);
        noteObtenue += choixObj.getCoeff();
        
        ReponseCandidat reponseCandidat = new ReponseCandidat();
        reponseCandidat.setCandidature(candidature); // Utiliser l'objet déjà récupéré
        reponseCandidat.setChoix(choixRepository.findById(idChoix).orElseThrow());
        
        reponseCandidatService.saveReponseCandidat(reponseCandidat);
    }
    
    for (Choix c : questionActuelle.getChoix()) {
        valeurMax += c.getCoeff();   
    }
    
    return noteObtenue / valeurMax ;
}

@Transactional
public Float evaluateQuestionnaire(Integer idCandidature,List<Float> notes) throws Exception
{

    Candidature candidature = candidatureRepository.findById(idCandidature)
        .orElseThrow(() -> new MyException("Candidature non trouvée avec l'ID: " + idCandidature));
    
    float moyenne = 0;
    for(Float note : notes)
    {
        moyenne+= note;
    }
    moyenne = moyenne / notes.size();

    System.out.println("Calcul de notes ok pour evaluation questionnaire:"+moyenne);
    Notes note = new Notes();
    note.setCandidature(candidature); // Utiliser l'objet déjà récupéré
    note.setEvaluation(evaluationRepository.findById(1).orElseThrow(()-> new MyException("ID d'evaluation inexistant")));
    System.out.println("Evaluation trouvee"); // le probleme reside ici vu qu'il n'y a encore rien dans la table Evaluation 
    // pb: le code s'arrete directement ici sans lever une seule exception
    note.setNote((double) moyenne);
    
    LocalDateTime dateActuelle = LocalDateTime.now();
    note.setDateEntree(dateActuelle);
    notesRepository.save(note);

    Integer nbJoursDecalage = 1 ;
    Float noteMinimale = 0.8f ; //sense provenir d'un fichier de conf

    if(moyenne>=noteMinimale)
    {
        Entretien entretien = new Entretien();
        entretien.setCandidature(candidature);
        entretien.setDateHeureDebut(dateActuelle.plusDays(nbJoursDecalage));
        //enregistre un entretien
        entretienService.saveEntretien(entretien);
    }
    //ici je vais donc envoyer un planning a l'email du candidat
        /* 
         * -contraintes: 
         *  firy ny note minimale dia alefa manao entretien
         *  firy ny jour de decalage 
         *  (aleo atao anaty fichier de configuration aloha)
         */
    //Alternative a voir: creer un ecran montrant les notes de chaque candidat avec l'option (Appeler pour l'entretien)



    return moyenne;
}
}
