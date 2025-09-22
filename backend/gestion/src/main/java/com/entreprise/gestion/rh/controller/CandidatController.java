package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.rh.dto.CandidatDTO;
import com.entreprise.gestion.rh.dto.EvaluationResultDTO;
import com.entreprise.gestion.rh.dto.NoteCreationDTO;
import com.entreprise.gestion.rh.model.Besoin;
import com.entreprise.gestion.rh.model.Candidat;
import com.entreprise.gestion.rh.model.CandidatCompetence;
import com.entreprise.gestion.rh.model.CandidatDiplomeFiliere;
import com.entreprise.gestion.rh.model.CandidatLangue;
import com.entreprise.gestion.rh.model.Candidature;
import com.entreprise.gestion.rh.model.Competence;
import com.entreprise.gestion.rh.model.DiplomeFiliere;
import com.entreprise.gestion.rh.model.Evaluation;
import com.entreprise.gestion.rh.model.Experience;
import com.entreprise.gestion.rh.model.Langue;
import com.entreprise.gestion.rh.model.Metier;
import com.entreprise.gestion.rh.model.Notes;
import com.entreprise.gestion.rh.service.CandidatService;
import com.entreprise.gestion.rh.service.NotesService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.entreprise.gestion.rh.model.Personne;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import com.entreprise.gestion.rh.repository.CompetenceRepository;
import com.entreprise.gestion.rh.repository.DiplomeFiliereRepository;
import com.entreprise.gestion.rh.repository.LangueRepository;
import com.entreprise.gestion.rh.repository.MetierRepository;
import com.entreprise.gestion.rh.service.EmailService;

@RestController
@RequestMapping("/api/candidat")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class CandidatController {
    
    private final CandidatService candidatService;
    private final CompetenceRepository competenceRepository;
    private final LangueRepository langueRepository;
    private final DiplomeFiliereRepository diplomeFiliereRepository;
    private final MetierRepository metierRepository;
       private final NotesService notesService;
       private final EmailService emailService;

    @PreAuthorize("hasRole('Candidat' or hasRole('Rh') or hasRole('Admin')")
    @PostMapping
    public ResponseEntity<CandidatDTO> createCandidat(@RequestBody CandidatDTO candidatDTO) {
        Candidat candidat = convertToEntity(candidatDTO);
        Candidat savedCandidat = candidatService.saveCandidat(candidat);
        return ResponseEntity.ok(convertToDTO(savedCandidat));
    }
  
    @PreAuthorize("hasRole('Candidat'")
    @PostMapping("/postuler/{besoinId}")
public ResponseEntity<EvaluationResultDTO> postuler(
        @PathVariable Integer besoinId,
        @RequestBody CandidatDTO candidatDTO) {
    
    Candidat candidat = convertToEntity(candidatDTO);
    candidat = candidatService.saveCandidat(candidat);

    Besoin besoin = candidatService.getBesoinById(besoinId);
    Candidature candidature = candidatService.createCandidature(besoin, candidat);

    // 3️⃣ Calculer la note
    int score = candidatService.calculateScore(candidat, besoin);
    
    // 🔥 CRÉER UN EvaluationResultDTO COMME /evaluate
    EvaluationResultDTO result = new EvaluationResultDTO();
    result.setCandidat(candidatDTO);
    
    Map<String, Integer> scores = new HashMap<>();
    scores.put(besoin.getMetier().getLibelle() + " - " + besoin.getDepartement().getLibelle(), score);
    result.setScores(scores);

    // 4️⃣ Sauvegarder la note
    Evaluation evaluation = candidatService.getEvaluationById(1);
    // notesService.saveNote((double)score, evaluation, candidature);

    NoteCreationDTO noteDTO = new NoteCreationDTO();
        noteDTO.setNote((double)score);
        noteDTO.setCandidatureId(candidature.getId());
        noteDTO.setEvaluationId(evaluation.getId());
        
        Notes note = notesService.saveNote(noteDTO);
    try {
       emailService.sendConfirmationEmail(candidat.getPersonne().getEmail(),candidat.getPersonne().getPrenom(),candidature.getId());
       
    } catch (Exception e) {
       // TODO: handle exception
    }

    // 🔥 RETOURNER EvaluationResultDTO AU LIEU DE Map
    return ResponseEntity.ok(result);
}

@PreAuthorize("hasRole('Candidat' or hasRole('Rh') or hasRole('Admin')")

    @PostMapping("/evaluate")
    public ResponseEntity<EvaluationResultDTO> evaluateCandidat(@RequestBody CandidatDTO candidatDTO) {
        Candidat candidat = convertToEntity(candidatDTO);
        
        EvaluationResultDTO result = new EvaluationResultDTO();
        result.setCandidat(candidatDTO);
        
        List<Besoin> besoins = candidatService.getActiveBesoins();
        Map<String, Integer> scores = new HashMap<>();
        
        for (Besoin besoin : besoins) {
            int score = candidatService.calculateScore(candidat, besoin);
            scores.put(besoin.getMetier().getLibelle() + " - " + besoin.getDepartement().getLibelle(), score);
        }
        
        result.setScores(scores);
        
        return ResponseEntity.ok(result);
    }
    
    @PreAuthorize("hasRole('isAuthenticated'")
    @GetMapping("/besoins")
    public ResponseEntity<List<Besoin>> getActiveBesoins() {
        List<Besoin> besoins = candidatService.getActiveBesoins();
        return ResponseEntity.ok(besoins);
    }
    
   private Candidat convertToEntity(CandidatDTO dto) {
    Candidat candidat = new Candidat();
    candidat.setId(dto.getId());
    candidat.setDescription(dto.getDescription());
    
    // Conversion de la personne
    if (dto.getPersonne() != null) {
        Personne personne = new Personne();
        personne.setId(dto.getPersonne().getId());
        personne.setNom(dto.getPersonne().getNom());
        personne.setPrenom(dto.getPersonne().getPrenom());
        personne.setEmail(dto.getPersonne().getEmail());
        personne.setDateNaissance(dto.getPersonne().getDateNaissance());
        personne.setGenre(dto.getPersonne().getGenre());
        personne.setVille(dto.getPersonne().getVille());
        personne.setTelephone(dto.getPersonne().getTelephone());
        candidat.setPersonne(personne);
    }
    
    // 🔥 CHARGEMENT DES COMPÉTENCES
    if (dto.getCompetencesIds() != null) {
        List<CandidatCompetence> competences = dto.getCompetencesIds().stream()
            .map(competenceId -> {
                Competence competence = competenceRepository.findById(competenceId).orElse(null);
                if (competence != null) {
                    CandidatCompetence cc = new CandidatCompetence();
                    cc.setCompetence(competence);
                    cc.setCandidat(candidat);
                    return cc;
                }
                return null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        candidat.setCompetences(competences);
    }
    
    // 🔥 CHARGEMENT DES LANGUES
    if (dto.getLanguesIds() != null) {
        List<CandidatLangue> langues = dto.getLanguesIds().stream()
            .map(langueId -> {
                Langue langue = langueRepository.findById(langueId).orElse(null);
                if (langue != null) {
                    CandidatLangue cl = new CandidatLangue();
                    cl.setLangue(langue);
                    cl.setCandidat(candidat);
                    return cl;
                }
                return null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        candidat.setLangues(langues);
    }
    
    // 🔥 CHARGEMENT DES DIPLÔMES
    if (dto.getDiplomesIds() != null) {
        List<CandidatDiplomeFiliere> diplomes = dto.getDiplomesIds().stream()
            .map(diplomeId -> {
                DiplomeFiliere diplomeFiliere = diplomeFiliereRepository.findById(diplomeId).orElse(null);
                if (diplomeFiliere != null) {
                    CandidatDiplomeFiliere cdf = new CandidatDiplomeFiliere();
                    cdf.setDiplomeFiliere(diplomeFiliere);
                    cdf.setCandidat(candidat);
                    return cdf;
                }
                return null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        candidat.setDiplomes(diplomes);
    }
    
    // 🔥 CHARGEMENT DES EXPÉRIENCES
    if (dto.getExperiences() != null) {
        List<Experience> experiences = dto.getExperiences().stream()
            .map(expDTO -> {
                Experience experience = new Experience();
                experience.setNbAnnee(expDTO.getNbAnnee());
                
                // Charger le métier depuis la base
                if (expDTO.getMetierId() != null) {
                    Metier metier = metierRepository.findById(expDTO.getMetierId()).orElse(null);
                    experience.setMetier(metier);
                }
                
                experience.setCandidat(candidat);
                return experience;
            })
            .collect(Collectors.toList());
        candidat.setExperiences(experiences);
    }
    
    return candidat;
}
    
    private CandidatDTO convertToDTO(Candidat candidat) {
        CandidatDTO dto = new CandidatDTO();
        dto.setId(candidat.getId());
        dto.setDescription(candidat.getDescription());
        
        if (candidat.getPersonne() != null) {
            CandidatDTO.PersonneDTO personneDTO = new CandidatDTO.PersonneDTO();
            personneDTO.setId(candidat.getPersonne().getId());
            personneDTO.setNom(candidat.getPersonne().getNom());
            personneDTO.setPrenom(candidat.getPersonne().getPrenom());
            personneDTO.setEmail(candidat.getPersonne().getEmail());
            personneDTO.setDateNaissance(candidat.getPersonne().getDateNaissance());
            personneDTO.setGenre(candidat.getPersonne().getGenre());
            personneDTO.setVille(candidat.getPersonne().getVille());
            personneDTO.setTelephone(candidat.getPersonne().getTelephone());
            dto.setPersonne(personneDTO);
        }
        
        return dto;
    }
}