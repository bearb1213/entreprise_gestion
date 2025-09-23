package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.model.Candidat;
import com.entreprise.gestion.rh.model.Competence;
import com.entreprise.gestion.rh.model.DiplomeFiliere;
import com.entreprise.gestion.rh.model.Experience;
import com.entreprise.gestion.rh.model.Langue;
import com.entreprise.gestion.rh.model.Personne;
import com.entreprise.gestion.rh.model.Besoin;
import com.entreprise.gestion.rh.model.Candidature;
import com.entreprise.gestion.rh.model.Entretien;


import com.entreprise.gestion.rh.repository.CandidatRepository;
import com.entreprise.gestion.rh.repository.CompetenceRepository;
import com.entreprise.gestion.rh.repository.DiplomeFiliereRepository;
import com.entreprise.gestion.rh.repository.LangueRepository;
import com.entreprise.gestion.rh.repository.PersonneRepository;
import com.entreprise.gestion.rh.repository.MetierRepository;
import com.entreprise.gestion.exception.MyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;


@Service
@RequiredArgsConstructor
public class CandidatService {

    private final PersonneRepository personneRepository;
    private final CandidatRepository candidatRepository;
    private final CompetenceRepository competenceRepository;
    private final LangueRepository langueRepository;
    private final DiplomeFiliereRepository diplomeFiliereRepository;
    private final MetierRepository metierRepository;

    @Transactional
    public void creerCandidat(Map<String, Object> data) throws MyException {
        try {
            System.out.println("=== DEBUG Données reçues ===");
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue() + " (type: " + 
                    (entry.getValue() != null ? entry.getValue().getClass().getName() : "null") + ")");
            }
            
            // Extraction et conversion des données
            String nom = (String) data.get("nom");
            String prenom = (String) data.get("prenom");
            String email = (String) data.get("email");
            String ville = (String) data.get("ville");
            String telephone = (String) data.get("telephone");
            String description = (String) data.get("description");
            
            // Conversion sécurisée du genre
            Object genreObj = data.get("genre");
            Integer genre;
            if (genreObj instanceof Integer) {
                genre = (Integer) genreObj;
            } else if (genreObj instanceof String) {
                try {
                    genre = Integer.parseInt((String) genreObj);
                } catch (NumberFormatException e) {
                    throw new MyException("Format de genre invalide: " + genreObj);
                }
            } else {
                throw new MyException("Format de genre invalide: " + genreObj);
            }
            
            // Conversion de la date de naissance
            LocalDate dateNaissance = LocalDate.parse((String) data.get("date_naissance"));
            
            // Vérification âge
            int age = LocalDate.now().getYear() - dateNaissance.getYear();
            if (age < 18 || age > 60) {
                throw new MyException("L'âge doit être compris entre 18 et 60 ans. Actuellement : " + age);
            }
            
            // Vérification email
            if (personneRepository.existsByEmail(email)) {
                throw new MyException("Une personne avec cet email existe déjà : " + email);
            }

            // Création de la personne
            Personne personne = Personne.builder()
                    .nom(nom)
                    .prenom(prenom)
                    .email(email)
                    .ville(ville)
                    .telephone(telephone)
                    .genre(genre) // Utilise la valeur convertie
                    .dateNaissance(dateNaissance)
                    .build();

            personneRepository.save(personne);

            // Création du candidat
            Candidat candidat = Candidat.builder()
                    .personne(personne)
                    .description(description)
                    .build();

            // --- Compétences ---
            List<?> competencesRaw = (List<?>) data.get("competences");
            List<Integer> competencesIds = new ArrayList<>();
            if (competencesRaw != null) {
                for (Object item : competencesRaw) {
                    if (item instanceof Integer) {
                        competencesIds.add((Integer) item);
                    } else if (item instanceof String) {
                        competencesIds.add(Integer.parseInt((String) item));
                    }
                }
            }

            if (!competencesIds.isEmpty()) {
                List<Competence> comps = new ArrayList<>();
                for (Integer id : competencesIds) {
                    competenceRepository.findById(id).ifPresent(comps::add);
                }
                candidat.setCompetences(comps);
            }

            // --- Langues ---
            List<?> languesRaw = (List<?>) data.get("langues");
            List<Integer> languesIds = new ArrayList<>();
            if (languesRaw != null) {
                for (Object item : languesRaw) {
                    if (item instanceof Integer) {
                        languesIds.add((Integer) item);
                    } else if (item instanceof String) {
                        languesIds.add(Integer.parseInt((String) item));
                    }
                }
            }

            if (!languesIds.isEmpty()) {
                List<Langue> langs = new ArrayList<>();
                for (Integer id : languesIds) {
                    langueRepository.findById(id).ifPresent(langs::add);
                }
                candidat.setLangues(langs);
            }

            // --- Diplômes filières ---
            List<?> diplomeRaw = (List<?>) data.get("filiere");
            List<Integer> diplomeIds = new ArrayList<>();
            if (diplomeRaw != null) {
                for (Object item : diplomeRaw) {
                    if (item instanceof Integer) {
                        diplomeIds.add((Integer) item);
                    } else if (item instanceof String) {
                        diplomeIds.add(Integer.parseInt((String) item));
                    }
                }
            }

            if (!diplomeIds.isEmpty()) {
                List<DiplomeFiliere> diplomes = new ArrayList<>();
                for (Integer id : diplomeIds) {
                    diplomeFiliereRepository.findById(id).ifPresent(diplomes::add);
                }
                candidat.setDiplomeFilieres(diplomes);
            }
                System.out.println("MIDITRA 01");

            // --- Expériences ---
            List<?> metiersRaw = (ArrayList<?>) data.get("metiers");
            List<Experience> experienceList = new ArrayList<>();
            
            if (metiersRaw != null) {
                for (Object item : metiersRaw) {
                    System.out.println("\n\n\n\n\n\n"+item.getClass().getName());
                    if (item instanceof Map) {
                        Map<String, Object> metierMap = (Map<String, Object>) item;
        
                        System.out.println("Clés de la map: " + metierMap.keySet());
                        System.out.println("Valeurs: metier=" + metierMap.get("metier") + ", experience=" + metierMap.get("experience"));
                        Object metierIdObj = metierMap.get("metier");
                        Object experienceObj = metierMap.get("experience");
                        
                        if (metierIdObj != null && experienceObj != null) {
                            Integer metierId=null;
                            Integer experience=null;
                            
                            // Conversion metierId
                            if (metierIdObj instanceof Integer) {
                                metierId = (Integer) metierIdObj;
                            } else if (metierIdObj instanceof String) {
                                metierId = Integer.parseInt((String) metierIdObj);
                            } else {
                                System.out.println("FORMAT INVALIDE");

                            }
                            
                            // Conversion experience
                            if (experienceObj instanceof Integer) {
                                experience = (Integer) experienceObj;
                            } else if (experienceObj instanceof String) {
                                experience = Integer.parseInt((String) experienceObj);
                            } else {
                                System.out.println("FORMAT INVALIDE");
                            }
                            
                            Experience exp = new Experience();
                            exp.setNbAnnee(experience);
                            metierRepository.findById(metierId).ifPresent(exp::setMetier);
                            exp.setCandidat(candidat);
                            
                            if (exp.getMetier() != null) {
                                experienceList.add(exp);
                                System.out.println("TEST RÉUSSI - Expérience ajoutée");
                            } else {
                                System.out.println("LAZAINY FA NULLLLL");
                            }
                        }
                    }
                }
            }

            candidat.setExperiences(experienceList);
            System.out.println("Experience : " + candidat.getExperiences().size());
            candidatRepository.save(candidat);

        } catch (NumberFormatException e) {
            throw new MyException("Erreur de format numérique: " + e.getMessage());
        } catch (MyException m) {
            throw m;
        } catch (Exception e) {
            throw new MyException("Erreur lors de la création du candidat: " + e.getMessage());
        }
    }

    public String saveFile(MultipartFile file, String folderPath) throws MyException {
        try {
            String tempDir = System.getProperty("java.io.tmpdir");
            Path folder = Paths.get( folderPath);

            if (!Files.exists(folder)) {
                Files.createDirectories(folder);
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = folder.resolve(fileName);

            // Copie plus fiable que transferTo
            try (InputStream is = file.getInputStream()) {
                Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            return filePath.toString();
        } catch (Exception e) {
            throw new MyException("Erreur lors de la sauvegarde du fichier : " + file.getOriginalFilename(), e);
        }
    }


    public Map<String, Object> listerCandidats() {
        List<Candidat> candidats = candidatRepository.findAll();
        Map<String, Object> result = new HashMap<>();
        
        List<Map<String, Object>> candidatsData = new ArrayList<>();

        for (Candidat c : candidats) {
            // Forcer l'initialisation des collections LAZY
            c.getCompetences().size();
            c.getLangues().size();
            c.getDiplomeFilieres().size();
            c.getCandidatures().size();
            c.getExperiences().size();

            // Construire l'objet candidat sous forme de Map
            Map<String, Object> candidatMap = new HashMap<>();
            
            // Informations de base du candidat
            candidatMap.put("id", c.getId());
            candidatMap.put("description", c.getDescription());
            
            // Informations de la personne
            Personne personne = c.getPersonne();
            if (personne != null) {
                Map<String, Object> personneMap = new HashMap<>();
                personneMap.put("id", personne.getId());
                personneMap.put("nom", personne.getNom());
                personneMap.put("prenom", personne.getPrenom());
                personneMap.put("email", personne.getEmail());
                personneMap.put("dateNaissance", personne.getDateNaissance());
                personneMap.put("genre", personne.getGenre());
                personneMap.put("ville", personne.getVille());
                personneMap.put("telephone", personne.getTelephone());
                personneMap.put("image", personne.getImage());
                candidatMap.put("personne", personneMap);
            }
            
            // Compétences (table candidat_competence)
            List<Map<String, Object>> competencesList = new ArrayList<>();
            for (Competence comp : c.getCompetences()) {
                Map<String, Object> compMap = new HashMap<>();
                compMap.put("id", comp.getId());
                compMap.put("libelle", comp.getLibelle());
                competencesList.add(compMap);
            }
            candidatMap.put("competences", competencesList);
            
            // Langues (table candidat_langue)
            List<Map<String, Object>> languesList = new ArrayList<>();
            for (Langue langue : c.getLangues()) {
                Map<String, Object> langueMap = new HashMap<>();
                langueMap.put("id", langue.getId());
                langueMap.put("libelle", langue.getLibelle());
                languesList.add(langueMap);
            }
            candidatMap.put("langues", languesList);
            
            // Diplômes filières (table candidat_diplome_filiere)
            List<Map<String, Object>> diplomesList = new ArrayList<>();
            for (DiplomeFiliere df : c.getDiplomeFilieres()) {
                Map<String, Object> diplomeMap = new HashMap<>();
                diplomeMap.put("id", df.getId());
                
                if (df.getFiliere() != null) {
                    Map<String, Object> filiereMap = new HashMap<>();
                    filiereMap.put("id", df.getFiliere().getId());
                    filiereMap.put("libelle", df.getFiliere().getLibelle());
                    diplomeMap.put("filiere", filiereMap);
                }
                
                if (df.getDiplome() != null) {
                    Map<String, Object> diplomeDetailMap = new HashMap<>();
                    diplomeDetailMap.put("id", df.getDiplome().getId());
                    diplomeDetailMap.put("libelle", df.getDiplome().getLibelle());
                    diplomeMap.put("diplome", diplomeDetailMap);
                }
                
                diplomesList.add(diplomeMap);
            }
            candidatMap.put("diplomeFilieres", diplomesList);
            
            // Expériences (table experience)
            List<Map<String, Object>> experiencesList = new ArrayList<>();
            for (Experience exp : c.getExperiences()) {
                Map<String, Object> expMap = new HashMap<>();
                expMap.put("id", exp.getId());
                expMap.put("nbAnnee", exp.getNbAnnee());
                
                if (exp.getMetier() != null) {
                    Map<String, Object> metierMap = new HashMap<>();
                    metierMap.put("id", exp.getMetier().getId());
                    metierMap.put("libelle", exp.getMetier().getLibelle());
                    expMap.put("metier", metierMap);
                }
                
                experiencesList.add(expMap);
            }
            candidatMap.put("experiences", experiencesList);
            
            // Candidatures (table candidature)
            List<Map<String, Object>> candidaturesList = new ArrayList<>();
            for (Candidature cand : c.getCandidatures()) {
                Map<String, Object> candMap = new HashMap<>();
                candMap.put("id", cand.getId());
                candMap.put("dateCandidature", cand.getDateCandidature());
                candMap.put("statut", cand.getStatut());
                
                // Informations sur le besoin associé
                if (cand.getBesoin() != null) {
                    Map<String, Object> besoinMap = new HashMap<>();
                    besoinMap.put("id", cand.getBesoin().getId());
                    besoinMap.put("statut", cand.getBesoin().getStatut());
                    besoinMap.put("minAge", cand.getBesoin().getMinAge());
                    besoinMap.put("maxAge", cand.getBesoin().getMaxAge());
                    besoinMap.put("nbPosteDispo", cand.getBesoin().getNbPosteDispo());
                    besoinMap.put("minExperience", cand.getBesoin().getMinExperience());
                    
                    // Métier du besoin
                    if (cand.getBesoin().getMetier() != null) {
                        Map<String, Object> metierMap = new HashMap<>();
                        metierMap.put("id", cand.getBesoin().getMetier().getId());
                        metierMap.put("libelle", cand.getBesoin().getMetier().getLibelle());
                        besoinMap.put("metier", metierMap);
                    }
                    
                    // Département du besoin
                    if (cand.getBesoin().getDepartement() != null) {
                        Map<String, Object> deptMap = new HashMap<>();
                        deptMap.put("id", cand.getBesoin().getDepartement().getId());
                        deptMap.put("libelle", cand.getBesoin().getDepartement().getLibelle());
                        besoinMap.put("departement", deptMap);
                    }
                    
                    candMap.put("besoin", besoinMap);
                }
                
                candidaturesList.add(candMap);
            }
            candidatMap.put("candidatures", candidaturesList);
            
            candidatsData.add(candidatMap);
        }

        // Construire le résultat final
        result.put("success", true);
        result.put("count", candidatsData.size());
        result.put("candidats", candidatsData);
        result.put("timestamp", LocalDateTime.now());
        
        return result;
    }


    public Candidat getCandidatById(Integer id) throws MyException {
        Candidat c = candidatRepository.findById(id)
                .orElseThrow(() -> new MyException("Candidat introuvable avec id=" + id));

        return c;
    }

    public Map<String, Object> getCandidatMap(Integer id) throws MyException {
        Candidat c = candidatRepository.findById(id)
                .orElseThrow(() -> new MyException("Candidat introuvable avec id=" + id));

        // Forcer l'initialisation des collections LAZY
        c.getCompetences().size();
        c.getLangues().size();
        c.getDiplomeFilieres().size();
        c.getCandidatures().size();
        c.getExperiences().size();

        // Construire l'objet candidat sous forme de Map
        Map<String, Object> candidatMap = new HashMap<>();
        
        // Informations de base du candidat
        candidatMap.put("id", c.getId());
        candidatMap.put("description", c.getDescription());
        
        // Informations de la personne
        Personne personne = c.getPersonne();
        if (personne != null) {
            Map<String, Object> personneMap = new HashMap<>();
            personneMap.put("id", personne.getId());
            personneMap.put("nom", personne.getNom());
            personneMap.put("prenom", personne.getPrenom());
            personneMap.put("email", personne.getEmail());
            personneMap.put("dateNaissance", personne.getDateNaissance());
            personneMap.put("genre", personne.getGenre());
            personneMap.put("ville", personne.getVille());
            personneMap.put("telephone", personne.getTelephone());
            personneMap.put("image", personne.getImage());
            candidatMap.put("personne", personneMap);
        }
        
        // Compétences
        List<Map<String, Object>> competencesList = new ArrayList<>();
        for (Competence comp : c.getCompetences()) {
            Map<String, Object> compMap = new HashMap<>();
            compMap.put("id", comp.getId());
            compMap.put("libelle", comp.getLibelle());
            competencesList.add(compMap);
        }
        candidatMap.put("competences", competencesList);
        
        // Langues
        List<Map<String, Object>> languesList = new ArrayList<>();
        for (Langue langue : c.getLangues()) {
            Map<String, Object> langueMap = new HashMap<>();
            langueMap.put("id", langue.getId());
            langueMap.put("libelle", langue.getLibelle());
            languesList.add(langueMap);
        }
        candidatMap.put("langues", languesList);
        
        // Diplômes filières
        List<Map<String, Object>> diplomesList = new ArrayList<>();
        for (DiplomeFiliere df : c.getDiplomeFilieres()) {
            Map<String, Object> diplomeMap = new HashMap<>();
            diplomeMap.put("id", df.getId());
            
            if (df.getFiliere() != null) {
                Map<String, Object> filiereMap = new HashMap<>();
                filiereMap.put("id", df.getFiliere().getId());
                filiereMap.put("libelle", df.getFiliere().getLibelle());
                diplomeMap.put("filiere", filiereMap);
            }
            
            if (df.getDiplome() != null) {
                Map<String, Object> diplomeDetailMap = new HashMap<>();
                diplomeDetailMap.put("id", df.getDiplome().getId());
                diplomeDetailMap.put("libelle", df.getDiplome().getLibelle());
                diplomeMap.put("diplome", diplomeDetailMap);
            }
            
            diplomesList.add(diplomeMap);
        }
        candidatMap.put("diplomeFilieres", diplomesList);
        
        // Expériences
        List<Map<String, Object>> experiencesList = new ArrayList<>();
        for (Experience exp : c.getExperiences()) {
            Map<String, Object> expMap = new HashMap<>();
            expMap.put("id", exp.getId());
            expMap.put("nbAnnee", exp.getNbAnnee());
            
            if (exp.getMetier() != null) {
                Map<String, Object> metierMap = new HashMap<>();
                metierMap.put("id", exp.getMetier().getId());
                metierMap.put("libelle", exp.getMetier().getLibelle());
                expMap.put("metier", metierMap);
            }
            
            experiencesList.add(expMap);
        }
        candidatMap.put("experiences", experiencesList);
        
        // Candidatures avec détails complets
        List<Map<String, Object>> candidaturesList = new ArrayList<>();
        for (Candidature cand : c.getCandidatures()) {
            Map<String, Object> candMap = new HashMap<>();
            candMap.put("id", cand.getId());
            candMap.put("dateCandidature", cand.getDateCandidature());
            candMap.put("statut", cand.getStatut());
            
            // Informations sur le besoin associé
            if (cand.getBesoin() != null) {
                Map<String, Object> besoinMap = new HashMap<>();
                besoinMap.put("id", cand.getBesoin().getId());
                besoinMap.put("statut", cand.getBesoin().getStatut());
                besoinMap.put("minAge", cand.getBesoin().getMinAge());
                besoinMap.put("maxAge", cand.getBesoin().getMaxAge());
                besoinMap.put("nbPosteDispo", cand.getBesoin().getNbPosteDispo());
                besoinMap.put("minExperience", cand.getBesoin().getMinExperience());
                besoinMap.put("coeffAge", cand.getBesoin().getCoeffAge());
                besoinMap.put("coeffExperience", cand.getBesoin().getCoeffExperience());
                
                // Métier du besoin
                if (cand.getBesoin().getMetier() != null) {
                    Map<String, Object> metierMap = new HashMap<>();
                    metierMap.put("id", cand.getBesoin().getMetier().getId());
                    metierMap.put("libelle", cand.getBesoin().getMetier().getLibelle());
                    besoinMap.put("metier", metierMap);
                }
                
                // Département du besoin
                if (cand.getBesoin().getDepartement() != null) {
                    Map<String, Object> deptMap = new HashMap<>();
                    deptMap.put("id", cand.getBesoin().getDepartement().getId());
                    deptMap.put("libelle", cand.getBesoin().getDepartement().getLibelle());
                    besoinMap.put("departement", deptMap);
                }
                
                candMap.put("besoin", besoinMap);
            }
            
            // Entretiens associés (si existent)
            if (cand.getEntretiens() != null && !cand.getEntretiens().isEmpty()) {
                List<Map<String, Object>> entretiensList = new ArrayList<>();
                for (Entretien ent : cand.getEntretiens()) {
                    Map<String, Object> entMap = new HashMap<>();
                    entMap.put("id", ent.getId());
                    entMap.put("dateHeureDebut", ent.getDateHeureDebut());
                    entretiensList.add(entMap);
                }
                candMap.put("entretiens", entretiensList);
            }
            
            candidaturesList.add(candMap);
        }
        candidatMap.put("candidatures", candidaturesList);

        // Ajouter des métadonnées
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("candidat", candidatMap);
        result.put("timestamp", LocalDateTime.now());
        
        return result;
    }

    public Candidat findByEmail(String email) throws MyException {
        return candidatRepository.findByEmail(email)
                .orElseThrow(() -> new MyException("Candidat introuvable avec email=" + email));
    }

    @Transactional(readOnly = true)
    public List<Candidat> filtrerCandidats(Map<String, Object> criteres) {
        List<Candidat> candidats = candidatRepository.findAll();

        // Filtrage par âge
        Integer minAge = (Integer) criteres.get("minAge");
        Integer maxAge = (Integer) criteres.get("maxAge");
        if (minAge != null || maxAge != null) {
            candidats = candidats.stream()
                .filter(c -> {
                    int age = LocalDate.now().getYear() - c.getPersonne().getDateNaissance().getYear();
                    boolean okMin = (minAge == null || age >= minAge);
                    boolean okMax = (maxAge == null || age <= maxAge);
                    return okMin && okMax;
                })
                .collect(Collectors.toList());
        }

        // Filtrage par ville
        String ville = (String) criteres.get("ville");
        if (ville != null) {
            candidats = candidats.stream()
                .filter(c -> ville.equalsIgnoreCase(c.getPersonne().getVille()))
                .collect(Collectors.toList());
        }

        // Filtrage par compétences (ids)
        List<Integer> competencesIds = (List<Integer>) criteres.get("competences");
        if (competencesIds != null && !competencesIds.isEmpty()) {
            candidats = candidats.stream()
                .filter(c -> c.getCompetences().stream()
                    .map(Competence::getId)
                    .collect(Collectors.toSet())
                    .containsAll(competencesIds))
                .collect(Collectors.toList());
        }

        // Filtrage par langues (ids)
        List<Integer> languesIds = (List<Integer>) criteres.get("langues");
        if (languesIds != null && !languesIds.isEmpty()) {
            candidats = candidats.stream()
                .filter(c -> c.getLangues().stream()
                    .map(Langue::getId)
                    .collect(Collectors.toSet())
                    .containsAll(languesIds))
                .collect(Collectors.toList());
        }

        // Filtrage par diplômes filières (ids)
        List<Integer> diplomeIds = (List<Integer>) criteres.get("diplomes");
        if (diplomeIds != null && !diplomeIds.isEmpty()) {
            candidats = candidats.stream()
                .filter(c -> c.getDiplomeFilieres().stream()
                    .map(DiplomeFiliere::getId)
                    .collect(Collectors.toSet())
                    .containsAll(diplomeIds))
                .collect(Collectors.toList());
        }

        // Filtrage par expériences (Map<metierId, minAnnee>)
        Map<Integer, Integer> experiences = (Map<Integer, Integer>) criteres.get("experiences");
        if (experiences != null && !experiences.isEmpty()) {
            candidats = candidats.stream()
                .filter(c -> {
                    Map<Integer, Integer> expMap = c.getExperiences().stream()
                            .collect(Collectors.toMap(e -> e.getMetier().getId(), Experience::getNbAnnee));
                    return experiences.entrySet().stream()
                            .allMatch(e -> expMap.getOrDefault(e.getKey(), 0) >= e.getValue());
                })
                .collect(Collectors.toList());
        }

        return candidats;
    }

    public List<Candidat> filtrerCandidatsParBesoinEtCritere(Besoin besoin, Map<String, Object> criteres) {
        // 1. Récupérer uniquement les candidats ayant déjà postulé à ce besoin
        List<Candidat> candidatsPostules = besoin.getCandidatures()
                                                .stream()
                                                .map(Candidature::getCandidat)
                                                .distinct()
                                                .collect(Collectors.toList());

        // 2. Appliquer les filtres selon les critères
        return candidatsPostules.stream().filter(candidat -> {

            // Filtre âge
            if (criteres.containsKey("ageMin") || criteres.containsKey("ageMax")) {
                LocalDate dateNaissance = candidat.getPersonne().getDateNaissance();
                int age = LocalDate.now().getYear() - dateNaissance.getYear();
                Integer ageMin = (Integer) criteres.get("ageMin");
                Integer ageMax = (Integer) criteres.get("ageMax");
                if ((ageMin != null && age < ageMin) || (ageMax != null && age > ageMax)) {
                    return false;
                }
            }

            // Filtre ville
            if (criteres.containsKey("ville")) {
                String ville = (String) criteres.get("ville");
                if (!candidat.getPersonne().getVille().equalsIgnoreCase(ville)) {
                    return false;
                }
            }

            // Filtre compétences
            if (criteres.containsKey("competences")) {
                List<Integer> competencesIds = (List<Integer>) criteres.get("competences");
                List<Integer> candidatCompIds = candidat.getCompetences()
                                                    .stream()
                                                    .map(Competence::getId)
                                                    .collect(Collectors.toList());
                if (!candidatCompIds.containsAll(competencesIds)) {
                    return false;
                }
            }

            // Filtre langues
            if (criteres.containsKey("langues")) {
                List<Integer> languesIds = (List<Integer>) criteres.get("langues");
                List<Integer> candidatLanguesIds = candidat.getLangues()
                                                        .stream()
                                                        .map(Langue::getId)
                                                        .collect(Collectors.toList());
                if (!candidatLanguesIds.containsAll(languesIds)) {
                    return false;
                }
            }

            // Filtre expériences
            if (criteres.containsKey("experiences")) {
                Map<Integer, Integer> expMap = (Map<Integer, Integer>) criteres.get("experiences");
                for (Map.Entry<Integer, Integer> entry : expMap.entrySet()) {
                    Integer metierId = entry.getKey();
                    Integer nbAnneeMin = entry.getValue();
                    boolean hasExp = candidat.getExperiences().stream()
                                            .anyMatch(exp -> exp.getMetier().getId().equals(metierId)
                                                            && exp.getNbAnnee() >= nbAnneeMin);
                    if (!hasExp) return false;
                }
            }

            // Filtre diplômes/filières
            if (criteres.containsKey("diplomes")) {
                List<Integer> diplomeIds = (List<Integer>) criteres.get("diplomes");
                List<Integer> candidatDiplomeIds = candidat.getDiplomeFilieres()
                                                        .stream()
                                                        .map(DiplomeFiliere::getId)
                                                        .collect(Collectors.toList());
                if (!candidatDiplomeIds.containsAll(diplomeIds)) {
                    return false;
                }
            }

            return true; // Si tous les critères sont respectés
        }).collect(Collectors.toList());
    }


    
}
