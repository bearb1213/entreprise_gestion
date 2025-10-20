package com.entreprise.gestion.rh.service;

<<<<<<< Updated upstream
=======
import java.time.LocalDateTime;
>>>>>>> Stashed changes
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
<<<<<<< Updated upstream

=======
import org.springframework.transaction.annotation.Transactional;

import com.entreprise.gestion.exception.MyException;
>>>>>>> Stashed changes
import com.entreprise.gestion.rh.dto.ChoixDto;
import com.entreprise.gestion.rh.dto.QuestionDto;
import com.entreprise.gestion.rh.model.Candidature;
import com.entreprise.gestion.rh.model.Choix;
<<<<<<< Updated upstream
=======
import com.entreprise.gestion.rh.model.Entretien;
>>>>>>> Stashed changes
import com.entreprise.gestion.rh.model.Notes;
import com.entreprise.gestion.rh.model.Question;
import com.entreprise.gestion.rh.model.ReponseCandidat;
import com.entreprise.gestion.rh.repository.CandidatureRepository;
import com.entreprise.gestion.rh.repository.ChoixRepository;
import com.entreprise.gestion.rh.repository.EvaluationRepository;
import com.entreprise.gestion.rh.repository.NotesRepository;
import com.entreprise.gestion.rh.repository.QuestionRepository;
<<<<<<< Updated upstream
=======
import com.entreprise.gestion.rh.repository.ReponseCandidatRepository;
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
=======
    @Autowired
    private ReponseCandidatRepository reponseCandidatRepository;

>>>>>>> Stashed changes
    //-- dependances de services
    @Autowired
    private ChoixService choixService;

<<<<<<< Updated upstream
=======
    @Autowired
    private EntretienService entretienService;

>>>>>>> Stashed changes
    @Autowired 
    private ReponseCandidatService reponseCandidatService;

    public Question findQuestionById(Integer id)
    {
        return questionRepository.findById(id).orElseThrow();
    }

<<<<<<< Updated upstream
=======
    public Question saveQuestion(Question question)
    {
        return questionRepository.save(question);
    }

>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
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
        .orElseThrow(() -> new EntityNotFoundException("Candidature non trouvée avec l'ID: " + idCandidature));
    
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

public Float evaluateQuestionnaire(Integer idCandidature,List<Float> notes) throws Exception
{

    Candidature candidature = candidatureRepository.findById(idCandidature)
        .orElseThrow(() -> new EntityNotFoundException("Candidature non trouvée avec l'ID: " + idCandidature));
    
    float moyenne = 0;
    for(Float note : notes)
    {
        moyenne+= note;
    }
    moyenne = moyenne / notes.size();

    System.out.println("Calcul de notes ok pour evaluation questionnaire:"+moyenne);
    Notes note = new Notes();
    note.setCandidature(candidature); // Utiliser l'objet déjà récupéré
    note.setEvaluation(evaluationRepository.findById(1).orElseThrow(()-> new Exception("ID d'evaluation inexistant")));
    System.out.println("Evaluation trouvee"); // le probleme reside ici vu qu'il n'y a encore rien dans la table Evaluation 
    // pb: le code s'arrete directement ici sans lever une seule exception
    note.setNote((double) moyenne);
    note.setDateEntree(java.time.LocalDateTime.now());
    notesRepository.save(note);


    return moyenne;
}
=======
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
            reponseCandidat.setDateHeure(LocalDateTime.now());
            
            reponseCandidatService.saveReponseCandidat(reponseCandidat);
        }
        
        for (Choix c : questionActuelle.getChoix()) {
            valeurMax += c.getCoeff();   
        }
        
        return noteObtenue ;
    }


    @Transactional
    public Float evaluateQuestionnaire(Integer idCandidature, List<Float> notes) throws Exception
    {
        Candidature candidature = candidatureRepository.findById(idCandidature)
            .orElseThrow(() -> new MyException("Candidature non trouvée avec l'ID: " + idCandidature));
        
        float moyenne = 0;
        for(Float note : notes)
        {
            moyenne+= note;
        }
        //moyenne = moyenne / notes.size();

        System.out.println("Calcul de notes ok pour evaluation questionnaire:" + moyenne);
        
        // CRÉATION DE LA NOTE AVEC EVALUATION ID=2 POUR LE QCM
        Notes note = new Notes();
        note.setCandidature(candidature);
        note.setEvaluation(evaluationRepository.findById(2) // ← CHANGEMENT ICI : ID=2 pour QCM
            .orElseThrow(() -> new MyException("Évaluation QCM (ID=2) non trouvée")));
        System.out.println("Evaluation QCM (ID=2) trouvée");
        note.setNote((double) moyenne);
        
        LocalDateTime dateActuelle = LocalDateTime.now();
        note.setDateEntree(dateActuelle);
        notesRepository.save(note);

        Integer nbJoursDecalage = 1;
        Float noteMinimale = 0.8f; // sensé provenir d'un fichier de conf

        if(moyenne >= noteMinimale)
        {
            Entretien entretien = new Entretien();
            entretien.setCandidature(candidature);
            entretien.setDateHeureDebut(dateActuelle.plusDays(nbJoursDecalage));
            // enregistre un entretien
            entretienService.saveEntretien(entretien);
        }
        // ici je vais donc envoyer un planning a l'email du candidat
            /* 
            * -contraintes: 
            *  firy ny note minimale dia alefa manao entretien
            *  firy ny jour de decalage 
            *  (aleo atao anaty fichier de configuration aloha)
            */
        // Alternative a voir: creer un ecran montrant les notes de chaque candidat avec l'option (Appeler pour l'entretien)

        return moyenne;
    }

        /**
     * Méthode pour calculer la note maximale possible d'un QCM
     * @param idCandidature ID de la candidature
     * @return La note maximale que le candidat peut obtenir au QCM
     */
    public Double getNoteMaximaleQCM(Integer idCandidature) throws MyException {
        try {
            Candidature candidature = candidatureRepository.findById(idCandidature)
                .orElseThrow(() -> new MyException("Candidature non trouvée avec l'ID: " + idCandidature));
            
            // Récupérer toutes les questions du QCM pour cette candidature
            List<Question> questions = getQuestionsPourQCMCandidature(candidature);


            Integer noteMaximaleTotale = 0;
            System.out.println();
            // Pour chaque question, calculer la note maximale possible
            for (Question question : questions) {
                if (question.getChoix() != null && !question.getChoix().isEmpty()) {
                    for (Choix choix : question.getChoix()) {
                        noteMaximaleTotale += choix.getCoeff();
                    }
                }
            }
            
            return noteMaximaleTotale.doubleValue();

        } catch (MyException e) {
            throw e;
        } catch (Exception e) {
            throw new MyException("Erreur lors du calcul de la note maximale du QCM", e);
        }
    }

    /**
     * Récupère les questions réellement utilisées pour une candidature spécifique
     * Cette méthode se base sur les réponses enregistrées du candidat
     * @param idCandidature ID de la candidature
     * @return Liste des questions auxquelles le candidat a répondu
     * @throws MyException Si la candidature n'existe pas ou en cas d'erreur
     */
    public List<Question> getQuestionsUtiliseesPourCandidature(Integer idCandidature) throws MyException {
        try {
            // Vérifier que la candidature existe
            Candidature candidature = candidatureRepository.findById(idCandidature)
                .orElseThrow(() -> new MyException("Candidature non trouvée avec l'ID: " + idCandidature));

            // Récupérer toutes les réponses du candidat pour cette candidature
            List<ReponseCandidat> reponsesCandidat = reponseCandidatRepository.findByCandidatureId(idCandidature);

            // Si le candidat n'a pas encore répondu, retourner une liste vide
            if (reponsesCandidat == null || reponsesCandidat.isEmpty()) {
                return Collections.emptyList();
            }

            // Extraire les questions à partir des réponses du candidat
            // On utilise distinct() pour éviter les doublons (plusieurs réponses à la même question)
            List<Question> questionsUtilisees = reponsesCandidat.stream()
                .map(ReponseCandidat::getChoix)           // Récupérer le choix
                .filter(choix -> choix != null)            // Filtrer les choix null
                .map(Choix::getQuestion)                   // Récupérer la question associée au choix
                .filter(question -> question != null)      // Filtrer les questions null
                .distinct()                                // Éliminer les doublons
                .collect(Collectors.toList());

            return questionsUtilisees;

        } catch (MyException e) {
            throw e;
        } catch (Exception e) {
            throw new MyException("Erreur lors de la récupération des questions utilisées pour la candidature", e);
        }
    }

    /**
     * Méthode utilitaire pour récupérer les questions d'un QCM pour une candidature
     */
    private List<Question> getQuestionsPourQCMCandidature(Candidature candidature) {
        // Essayer d'abord de récupérer les questions réellement utilisées
        try {
            List<Question> questionsUtilisees = getQuestionsUtiliseesPourCandidature(candidature.getId());
            if (questionsUtilisees != null && !questionsUtilisees.isEmpty()) {
                return questionsUtilisees;
            }
        } catch (MyException e) {
            // Si erreur, continuer avec la logique par défaut
            System.out.println("Impossible de récupérer les questions utilisées: " + e.getMessage());
        }

        // Logique par défaut si aucune réponse n'a été trouvée
        Integer nbQuestions = 10;
        List<Question> questions = Collections.emptyList();
        
        if (candidature.getBesoin() != null && candidature.getBesoin().getDepartement() != null) {
            questions = getQuestionsAleatoiresParDepartement(
                candidature.getBesoin().getDepartement().getId(),
                nbQuestions
            );
        } else if (candidature.getBesoin() != null && candidature.getBesoin().getMetier() != null) {
            questions = getQuestionsAleatoiresParMetier(
                candidature.getBesoin().getMetier().getId(),
                nbQuestions
            );
        } else {
            questions = getQuestionsAleatoiresGenerales(nbQuestions);
        }
        
        return questions;
    }
>>>>>>> Stashed changes
}
