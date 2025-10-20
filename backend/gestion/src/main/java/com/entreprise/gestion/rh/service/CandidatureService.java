package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.exception.MyException;
import com.entreprise.gestion.rh.model.Besoin;
import com.entreprise.gestion.rh.model.Candidat;
import com.entreprise.gestion.rh.model.Candidature;
import com.entreprise.gestion.rh.model.Personne;
import com.entreprise.gestion.rh.model.Notes;
import com.entreprise.gestion.rh.model.Evaluation;
import com.entreprise.gestion.rh.repository.BesoinRepository;
import com.entreprise.gestion.rh.repository.CandidatRepository;
import com.entreprise.gestion.rh.repository.CandidatureRepository;
import com.entreprise.gestion.rh.repository.EmployeRepository;
import com.entreprise.gestion.rh.repository.EvaluationRepository;
import com.entreprise.gestion.rh.dto.CandidatureSimpleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CandidatureService {

    private final CandidatRepository candidatRepository;
    private final BesoinRepository besoinRepository;
    private final CandidatureRepository candidatureRepository;
    private final EmployeRepository employeRepository;
    private final EvaluationRepository evaluationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final CandidatureEvaluationService evaluationService;
    private final StatusCandidatureService statusCandidatureService;
    private final EmailService emailService;
    private final QuestionService questionService;

    @Transactional
    public void candidater(String mail, Integer idBesoin) throws MyException {
        try {
            // Vérification du candidat
            Candidat candidat = candidatRepository.findByEmail(mail)
                    .orElseThrow(() -> new MyException("Veuillez creer votre CV avec l'email " + mail));

            // Vérification besoin
            Besoin besoin = besoinRepository.findById(idBesoin)
                    .orElseThrow(() -> new MyException("Besoin introuvable avec id=" + idBesoin));

            // Vérifier si le candidat a déjà postulé
            boolean dejaCandidat = candidatureRepository.existsByCandidatIdAndBesoinId(candidat.getId(), idBesoin);
            if (dejaCandidat) {
                throw new MyException("Vous avez déjà postulé à ce poste");
            }

            // Créer la candidature
            Candidature candidature = Candidature.builder()
                    .candidat(candidat)
                    .besoin(besoin)
                    .dateCandidature(LocalDateTime.now())
                    .statut(0) // 0 = en attente
                    .build();

            Candidature savedCandidature = candidatureRepository.save(candidature);

            Double noteObtenue = evaluationService.calculerNoteCandidature(candidature.getCandidat(),candidature.getBesoin());
            Double noteMaximale = calculerNoteMaximale(candidature.getBesoin());

            evaluerEtMettreAJourStatut(savedCandidature.getId());

            // Notification
            String notifMessage = "Nouveau candidat pour le poste : " + besoin.getMetier().getLibelle() +
                                  " (Candidat : " + candidat.getPersonne().getNom() + " " +
                                  candidat.getPersonne().getPrenom() + ") - Note: " + 
                                  (noteObtenue != null ? String.format("%.2f", noteObtenue) + "/" + 
                                  String.format("%.2f", noteMaximale) + 
                                  " (" + String.format("%.1f", (noteObtenue/noteMaximale)*100) + "%)" : "Non évalué");
            messagingTemplate.convertAndSend("/topic/notifications", notifMessage);

        } catch (MyException e) {
            throw e;
        } catch (Exception e) {
            throw new MyException("Erreur lors de la candidature", e);
        }
    }

    private Double calculerNoteMaximale(Besoin besoin) {
        Double noteMax = 0.0;
        
        // Coefficients de base
        if (besoin.getCoeffAge() != null) {
            noteMax += besoin.getCoeffAge();
        }
        if (besoin.getCoeffExperience() != null) {
            noteMax += besoin.getCoeffExperience();
        }
        
        // Coefficients des compétences
        if (besoin.getBesoinCompetences() != null) {
            for (var besoinCompetence : besoin.getBesoinCompetences()) {
                if (besoinCompetence.getCoeff() != null) {
                    noteMax += besoinCompetence.getCoeff();
                }
            }
        }
        
        // Coefficients des langues
        if (besoin.getBesoinLangues() != null) {
            for (var besoinLangue : besoin.getBesoinLangues()) {
                if (besoinLangue.getCoeff() != null) {
                    noteMax += besoinLangue.getCoeff();
                }
            }
        }
        
        // Coefficients des diplômes
        if (besoin.getBesoinDiplomeFilieres() != null) {
            for (var besoinDiplome : besoin.getBesoinDiplomeFilieres()) {
                if (besoinDiplome.getCoeff() != null) {
                    noteMax += besoinDiplome.getCoeff();
                }
            }
        }
        
        return noteMax;
    }

    private void envoyerEmailInvitationQCM(Candidature candidature) {
        try {
            Personne personne = candidature.getCandidat().getPersonne();
            String email = personne.getEmail();
            String prenom = personne.getPrenom();
            
            Integer candidatureId = candidature.getId();
            
            // Générer le lien avec tous les paramètres nécessaires
            String lienQCM = "http://localhost:5173/questionnaire/"+ candidatureId;
            
            emailService.envoyerEmailAvecLien(email, prenom, lienQCM);
            
            System.out.println("Email QCM envoyé à : " + email + " pour la candidature #" + candidatureId);
            
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email QCM : " + e.getMessage());
        }
    }

    /**
     * Méthode pour évaluer une candidature existante et mettre à jour son statut
     */
    @Transactional
    public void evaluerEtMettreAJourStatut(Integer candidatureId) throws MyException {
        try {
            Candidature candidature = candidatureRepository.findById(candidatureId)
                    .orElseThrow(() -> new MyException("Candidature introuvable avec id=" + candidatureId));

            // Réévaluation automatique
            evaluationService.evaluerCandidature(candidatureId);

            // Récupérer la note et calculer le pourcentage
            Double noteObtenue = evaluationService.calculerNoteCandidature(candidature.getCandidat(), candidature.getBesoin());
            Double noteMaximale = calculerNoteMaximale(candidature.getBesoin());
            
            if (noteObtenue != null && noteMaximale > 0) {
                double pourcentage = (noteObtenue / noteMaximale) * 100;
                Integer ancienStatut = candidature.getStatut();
                Integer nouveauStatut;
                
                // Déterminer le nouveau statut
                if (pourcentage >= 75) {
                    nouveauStatut = 2; // Acceptée
                    envoyerEmailInvitationQCM(candidature);
                } else if (pourcentage >= 50) {
                    nouveauStatut = 1; // En étude
                } else {
                    nouveauStatut = 3; // Refusée
                }
                
                // Mettre à jour le statut seulement s'il a changé
                if (!ancienStatut.equals(nouveauStatut)) {
                    candidature.setStatut(nouveauStatut);
                    candidatureRepository.save(candidature);
                    
                    // Créer un status_candidature avec evaluation_id approprié
                    Integer evaluationId = 1; // Évaluation initiale des compétences
                    statusCandidatureService.createStatusCandidature(candidatureId, evaluationId);
                    
                    // Notification de changement de statut
                    String statutMessage = getStatutMessage(nouveauStatut);
                    String notifMessage = "Changement de statut pour la candidature #" + candidatureId + 
                                        " : " + statutMessage + " (Note: " + 
                                        String.format("%.2f", noteObtenue) + "/" + 
                                        String.format("%.2f", noteMaximale) + 
                                        " - " + String.format("%.1f", pourcentage) + "%)";
                    messagingTemplate.convertAndSend("/topic/notifications", notifMessage);
                }
            }
            
        } catch (MyException e) {
            throw e;
        } catch (Exception e) {
            throw new MyException("Erreur lors de l'évaluation de la candidature", e);
        }
    }

    @Transactional
    public void updateStatutCandidature(Integer candidatureId, Integer nouveauStatut) throws MyException {
        try {
            Candidature candidature = candidatureRepository.findById(candidatureId)
                    .orElseThrow(() -> new MyException("Candidature introuvable avec id=" + candidatureId));

            Integer ancienStatut = candidature.getStatut();
            
            // Vérifier si le statut a changé
                candidature.setStatut(nouveauStatut);
                candidatureRepository.save(candidature);
                
                // Créer un status_candidature pour le changement manuel
                Integer evaluationId = 2; // Évaluation manuelle
                statusCandidatureService.createStatusCandidature(candidatureId, evaluationId);
                
                // Notification
                String statutMessage = getStatutMessage(nouveauStatut);
                String notifMessage = "Changement de statut manuel pour la candidature #" + candidatureId + 
                                    " : " + statutMessage + 
                                    " (Candidat: " + candidature.getCandidat().getPersonne().getPrenom() + " " + 
                                    candidature.getCandidat().getPersonne().getNom() + ")";
                messagingTemplate.convertAndSend("/topic/notifications", notifMessage);
                
                // Envoyer un email au candidat si le statut est "Acceptée" (2)
                if (nouveauStatut == 2) {
                    envoyerEmailInvitationQCM(candidature);
                }
                
                System.out.println("Statut de la candidature #" + candidatureId + " mis à jour : " + 
                                ancienStatut + " → " + nouveauStatut);
            
        } catch (MyException e) {
            throw e;
        } catch (Exception e) {
            throw new MyException("Erreur lors de la mise à jour du statut de la candidature", e);
        }
    }

    private String getStatutMessage(Integer statut) {
        switch (statut) {
            case 0: return "En attente";
            case 1: return "En etude";
            case 2: return "Acceptée";
            case 3: return "Refusée";
            default: return "Inconnu";
        }
    }

    private CandidatureSimpleDTO convertToSimpleDTO(Candidature candidature) {
        // Récupérer le titre du besoin
        String besoinTitre = "N/A";
        if (candidature.getBesoin() != null) {
            besoinTitre = candidature.getBesoin().getMetier() != null ? 
                        candidature.getBesoin().getMetier().getLibelle() : 
                        "Besoin sans métier";
        }
        
        // Récupérer le nom du candidat
        String candidatNom = "N/A";
        if (candidature.getCandidat() != null && candidature.getCandidat().getPersonne() != null) {
            candidatNom = candidature.getCandidat().getPersonne().getPrenom() + " " + 
                        candidature.getCandidat().getPersonne().getNom();
        }
        
        // Récupérer le poste (métier)
        String poste = "N/A";
        if (candidature.getBesoin() != null && candidature.getBesoin().getMetier() != null) {
            poste = candidature.getBesoin().getMetier().getLibelle();
        }
        
        return new CandidatureSimpleDTO(
            candidature.getId(),
            candidature.getStatut(),
            candidature.getDateCandidature(),
            besoinTitre,
            candidatNom,
            poste
        );
    }

    public Candidature findCandidatureById(Integer id) throws Exception {
        return candidatureRepository.findById(id).orElseThrow(()->new MyException("Candidature introuvable"));
    }
  
    /**
     * Récupérer toutes les candidatures avec conversion en DTO simplifié
     */
    public List<CandidatureSimpleDTO> getAllCandidatures() {
        return candidatureRepository.findAll().stream()
                .map(this::convertToSimpleDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer une candidature par son ID en DTO simplifié
     */
    public CandidatureSimpleDTO getCandidatureById(Integer id) {
        Candidature candidature = candidatureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidature non trouvée avec l'ID: " + id));
        return convertToSimpleDTO(candidature);
    }

    /**
     * Récupérer les candidatures par statut en DTO simplifié
     */
    public List<CandidatureSimpleDTO> getCandidaturesByStatut(Integer statut) {
        return candidatureRepository.findByStatut(statut).stream()
                .map(this::convertToSimpleDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les candidatures par besoin en DTO simplifié
     */
    public List<CandidatureSimpleDTO> getCandidaturesByBesoin(Integer besoinId) {
        return candidatureRepository.findByBesoinId(besoinId).stream()
                .map(this::convertToSimpleDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les candidatures par candidat en DTO simplifié
     */
    public List<CandidatureSimpleDTO> getCandidaturesByCandidat(Integer candidatId) {
        return candidatureRepository.findByCandidatId(candidatId).stream()
                .map(this::convertToSimpleDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getCandidatureDetails(Integer candidatureId) throws MyException {
        try {
            Candidature candidature = candidatureRepository.findById(candidatureId)
                    .orElseThrow(() -> new MyException("Candidature introuvable avec id=" + candidatureId));

            Map<String, Object> result = new HashMap<>();
            result.put("id", candidature.getId());
            result.put("statut", candidature.getStatut());
            result.put("dateCandidature", candidature.getDateCandidature());

            // Infos candidat
            if (candidature.getCandidat() != null && candidature.getCandidat().getPersonne() != null) {
                Personne personne = candidature.getCandidat().getPersonne();
                result.put("candidat", personne.getPrenom() + " " + personne.getNom());
                result.put("email", personne.getEmail());
                result.put("date_naissance", personne.getDateNaissance());
                result.put("telephone", personne.getTelephone());
                result.put("ville", personne.getVille());
                result.put("description", candidature.getCandidat().getDescription());
                
                // Genre
                if (personne.getGenre() != null) {
                    result.put("genre", personne.getGenre());
                }
            }

            // VÉRIFIER SI LE CANDIDAT EST DÉJÀ EMPLOYÉ
            boolean estDejaEmploye = false;
            if (candidature.getCandidat() != null && candidature.getCandidat().getPersonne() != null) {
                estDejaEmploye = employeRepository.existsByPersonneId(candidature.getCandidat().getPersonne().getId());
            }
            result.put("estDejaEmploye", estDejaEmploye);

            // Infos besoin avec département
            if (candidature.getBesoin() != null) {
                Besoin besoin = candidature.getBesoin();
                Map<String, Object> besoinMap = new HashMap<>();
                besoinMap.put("id", besoin.getId());
                
                if (besoin.getMetier() != null) {
                    besoinMap.put("metier", besoin.getMetier().getLibelle());
                }
                
                if (besoin.getDepartement() != null) {
                    besoinMap.put("departement", besoin.getDepartement().getLibelle());
                    result.put("departement", besoin.getDepartement().getLibelle());
                }
                
                besoinMap.put("nbPosteDispo", besoin.getNbPosteDispo());
                besoinMap.put("minAge", besoin.getMinAge());
                besoinMap.put("maxAge", besoin.getMaxAge());
                besoinMap.put("minExperience", besoin.getMinExperience());
                besoinMap.put("statut", besoin.getStatut());
                
                result.put("besoin", besoinMap);
                result.put("besoinId", besoin.getId());
            }

            // Notes groupées par évaluation avec note maximale par évaluation
            List<Map<String, Object>> notesDetails = candidature.getNotes().stream()
                    .map(note -> {
                        Map<String, Object> noteMap = new HashMap<>();
                        noteMap.put("evaluationId", note.getEvaluation() != null ? note.getEvaluation().getId() : null);
                        noteMap.put("evaluation", note.getEvaluation() != null ? note.getEvaluation().getLibelle() : "N/A");
                        noteMap.put("note", note.getNote());
                        noteMap.put("date", note.getDateEntree());
                        
                        Double noteMaximaleEvaluation = null;
                        try {
                            noteMaximaleEvaluation = getNoteMaximaleParEvaluation(note.getEvaluation(), candidature);
                        } catch (MyException e) {
                            System.err.println("Erreur lors du calcul de la note maximale : " + e.getMessage());
                        }

                        
                        // Calcul du pourcentage pour cette évaluation
                        if (note.getNote() != null && noteMaximaleEvaluation != null && noteMaximaleEvaluation > 0) {
                            double pourcentage = (note.getNote() / noteMaximaleEvaluation) * 100;
                            noteMap.put("pourcentage", Math.round(pourcentage * 100.0) / 100.0);
                        }
                        noteMap.put("noteMaximal",noteMaximaleEvaluation);
                        
                        return noteMap;
                    })
                    .collect(Collectors.toList());

            result.put("notes", notesDetails);

            // CALCUL DE LA NOTE MAXIMALE TOTALE POUR LE CV
            Double noteMaximaleCV = calculerNoteMaximale(candidature.getBesoin());
            result.put("noteMaximale", noteMaximaleCV);
            
            // Calcul si le candidat a passé l'évaluation CV (>= 75%)
            Double noteCV = evaluationService.calculerNoteCandidature(candidature.getCandidat(), candidature.getBesoin());
            boolean estPasseCV = noteCV != null && noteCV >= 75 * noteMaximaleCV / 100;
            result.put("estPasse", estPasseCV);

            // CALCUL DE LA NOTE TOTALE (dernière note)
            Double noteTotale = calculerNoteTotale(candidature);
            
            // CALCUL DU POURCENTAGE TOTAL
            if (noteTotale != null && noteMaximaleCV != null && noteMaximaleCV > 0) {
                double pourcentage = (noteTotale / noteMaximaleCV) * 100;
                result.put("pourcentage", Math.round(pourcentage * 100.0) / 100.0);
            }

            // AJOUT: ID de la dernière évaluation passée
            Integer derniereEvaluationId = getDerniereEvaluationPassee(candidature);
            result.put("derniereEvaluationId", derniereEvaluationId);

            return result;

        } catch (MyException e) {
            throw e;
        } catch (Exception e) {
            throw new MyException("Erreur lors de la récupération des détails de la candidature", e);
        }
    }

    /**
     * Méthode pour obtenir la note maximale par type d'évaluation
     */
    private Double getNoteMaximaleParEvaluation(Evaluation evaluation,Candidature c) throws MyException {
        if (evaluation == null) {
            return null;
        }
        
        // Logique pour déterminer la note maximale selon le type d'évaluation
        String libelle = evaluation.getLibelle().toLowerCase();
        
        if (libelle.contains("cv") || libelle.contains("compétence")) {
            return calculerNoteMaximale(c.getBesoin()); // Pour l'évaluation CV
        } else if (libelle.contains("qcm") || libelle.contains("test") || libelle.contains("questionnaire")) {
            return questionService.getNoteMaximaleQCM(c.getId()); // Pour le QCM
        } else if (libelle.contains("entretien")) {
            return 20.0; // Pour l'entretien
        } else {
            return 100.0; // Valeur par défaut
        }
    }

    /**
     * Récupère l'ID de la dernière évaluation passée (note >= 75%)
     */
    private Integer getDerniereEvaluationPassee(Candidature candidature) throws MyException {
        if (candidature.getNotes() == null || candidature.getNotes().isEmpty()) {
            return null;
        }

        Optional<Notes> derniereNotePassee = candidature.getNotes().stream()
                .filter(note -> note.getEvaluation() != null && note.getNote() != null)
                .filter(note -> {
                    try {
                        Double noteMaximaleEvaluation = getNoteMaximaleParEvaluation(note.getEvaluation(), candidature);
                        if (noteMaximaleEvaluation == null || noteMaximaleEvaluation == 0) return false;

                        double pourcentage = (note.getNote() / noteMaximaleEvaluation) * 100;
                        return pourcentage>=0;
                    } catch (MyException e) {
                        // Si une exception survient, on exclut cette note du filtre
                        return false;
                    }
                })
                .max(Comparator.comparing(Notes::getDateEntree));

        return derniereNotePassee
                .map(note -> note.getEvaluation().getId())
                .orElse(null);
    }


    private Double calculerNoteTotale(Candidature candidature) {
        if (candidature.getNotes() == null || candidature.getNotes().isEmpty()) {
            return 0.0;
        }
        
        return candidature.getNotes().stream()
                .max(Comparator.comparing(Notes::getDateEntree))
                .map(Notes::getNote)
                .orElse(0.0);
    }
}
