package com.entreprise.gestion.rh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.entreprise.gestion.exception.MyException;
import com.entreprise.gestion.rh.repository.UtilisateurRepository;
import com.entreprise.gestion.rh.repository.CandidatureRepository;

import com.entreprise.gestion.rh.model.Candidat;
import com.entreprise.gestion.rh.model.Entretien;
import com.entreprise.gestion.rh.model.Utilisateur;
import com.entreprise.gestion.rh.model.Candidature;
import com.entreprise.gestion.rh.repository.EntretienRepository;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;


@Service
public class EntretienService {
    
    @Autowired 
    private EntretienRepository entretienRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private CandidatureRepository candidatureRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public Entretien createEntretien(Integer rhId, Integer candidatureId, LocalDateTime dateHeureDebut, LocalDateTime dateHeureFin) throws Exception {
        // Charger les entités depuis la base
        Utilisateur rh = utilisateurRepository.findById(rhId)
            .orElseThrow(() -> new MyException("RH non trouvé avec l'ID: " + rhId));
        
        Candidature candidature = candidatureRepository.findById(candidatureId)
            .orElseThrow(() -> new MyException("Candidature non trouvée avec l'ID: " + candidatureId));

        // Créer l'entretien
        Entretien entretien = new Entretien();
        entretien.setRh(rh);
        entretien.setCandidature(candidature);
        entretien.setDateHeureDebut(dateHeureDebut);
        entretien.setDateHeureFin(dateHeureFin);

        // Sauvegarder (avec vérification des chevauchements)
        return saveEntretien(entretien);
    }

    @Transactional
    public Entretien saveEntretien(Entretien entretien) throws Exception {
        // Vérifier les chevauchements avant de sauvegarder
        if (hasChevauchement(entretien)) {
            throw new MyException("❌ Cet entretien chevauche avec un entretien existant pour ce RH");
        }
        
        sendMailEntretien(entretien);
        return entretienRepository.save(entretien);
    }

    /**
     * Vérifie si un entretien chevauche avec des entretiens existants pour le même RH
     */
    private boolean hasChevauchement(Entretien nouvelEntretien) {
        if (nouvelEntretien.getRh() == null || nouvelEntretien.getDateHeureDebut() == null || nouvelEntretien.getDateHeureFin() == null) {
            return false;
        }

        Integer rhId = nouvelEntretien.getRh().getId();
        LocalDateTime debutNouveau = nouvelEntretien.getDateHeureDebut();
        LocalDateTime finNouveau = nouvelEntretien.getDateHeureFin();

        // Récupérer tous les entretiens du même RH qui pourraient chevaucher
        List<Entretien> entretiensExistants = entretienRepository.findByRhIdAndDateHeureDebutBetween(
            rhId, 
            debutNouveau.minusDays(1), // Recherche large pour couvrir tous les cas
            finNouveau.plusDays(1)
        );

        // Vérifier les chevauchements
        for (Entretien existant : entretiensExistants) {
            if (entretiensSeChevauchent(debutNouveau, finNouveau, existant.getDateHeureDebut(), existant.getDateHeureFin())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Vérifie si deux plages horaires se chevauchent
     */
    private boolean entretiensSeChevauchent(LocalDateTime debut1, LocalDateTime fin1, LocalDateTime debut2, LocalDateTime fin2) {
        // Vérifie si les plages se chevauchent
        return debut1.isBefore(fin2) && fin1.isAfter(debut2);
    }

    public void sendMailEntretien(Entretien entretien) throws Exception
    {
        Candidat candidat = entretien.getCandidature().getCandidat();
        String to = candidat.getPersonne().getEmail();
        String nomCandidat = candidat.getPersonne().getNom()+" "+candidat.getPersonne().getPrenom();
        String objet = "Entretien - Système RH";
        String dateEntretien = entretien.getDateHeureDebut().toLocalDate().toString();
        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: #007bff; color: white; padding: 20px; text-align: center; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1> Candidature retenue </h1>
                    </div>
                    <div>
                        <h2>Bonjour, %s !</h2>
                        <p>Felicitations, votre dossier a été selectionné pour la phase d'entretien</p>
                        <p>Nous vous prions de vous rendre à nos locaux le %s. à nos heures d'ouverture habituelles</p>
                        <p>Cordialement.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(nomCandidat,dateEntretien);
        
        emailService.sendHtmlEmail(to, objet, htmlContent);
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getEntretiensFutursByRhId(Integer rhId) {
        try {
            LocalDateTime maintenant = LocalDateTime.now();
            List<Entretien> entretiens = entretienRepository.findByRhIdAndDateHeureDebutAfter(rhId, maintenant);
            
            return entretiens.stream()
                    .map(this::convertEntretienToMap)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des entretiens futurs pour le RH ID: " + rhId, e);
        }
    }

    /**
     * Convertit un entretien en Map pour la réponse API
     */
    private Map<String, Object> convertEntretienToMap(Entretien entretien) {
        Map<String, Object> entretienMap = new HashMap<>();
        
        // Informations de base de l'entretien
        entretienMap.put("id", entretien.getId());
        entretienMap.put("dateHeureDebut", entretien.getDateHeureDebut());
        entretienMap.put("dateHeureFin", entretien.getDateHeureFin());
        
        // Informations de la candidature et du candidat
        if (entretien.getCandidature() != null) {
            // AJOUT: ID de la candidature
            entretienMap.put("candidatureId", entretien.getCandidature().getId());
            
            if (entretien.getCandidature().getCandidat() != null) {
                Candidat candidat = entretien.getCandidature().getCandidat();
                
                // Informations du candidat
                Map<String, Object> candidatMap = new HashMap<>();
                if (candidat.getPersonne() != null) {
                    candidatMap.put("nom", candidat.getPersonne().getNom());
                    candidatMap.put("prenom", candidat.getPersonne().getPrenom());
                    candidatMap.put("email", candidat.getPersonne().getEmail());
                    candidatMap.put("telephone", candidat.getPersonne().getTelephone());
                }
                entretienMap.put("candidat", candidatMap);
                
                // Informations du poste
                if (entretien.getCandidature().getBesoin() != null && 
                    entretien.getCandidature().getBesoin().getMetier() != null) {
                    entretienMap.put("poste", entretien.getCandidature().getBesoin().getMetier().getLibelle());
                }
            }
        }
        
        return entretienMap;
    }
}