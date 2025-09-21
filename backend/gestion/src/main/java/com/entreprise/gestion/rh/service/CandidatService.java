package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.model.Candidat;
import com.entreprise.gestion.rh.model.Competence;
import com.entreprise.gestion.rh.model.DiplomeFiliere;
import com.entreprise.gestion.rh.model.Experience;
import com.entreprise.gestion.rh.model.Langue;
import com.entreprise.gestion.rh.model.Personne;
import com.entreprise.gestion.rh.model.Besoin;
import com.entreprise.gestion.rh.model.Candidature;

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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



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

            String nom = (String) data.get("nom");
            String prenom = (String) data.get("prenom");
            String email = (String) data.get("email");

            if (personneRepository.existsByEmail(email)) {
                throw new MyException("Une personne avec cet email existe déjà : " + email);
            }

            // Vérification âge
            LocalDate dateNaissance = LocalDate.parse((String) data.get("date_naissance"));
            int age = LocalDate.now().getYear() - dateNaissance.getYear();
            if (age < 18 || age > 60) {
                throw new MyException("L'âge doit être compris entre 18 et 60 ans. Actuellement : " + age);
            }

            // Création de la personne
            Personne personne = Personne.builder()
                    .nom(nom)
                    .prenom(prenom)
                    .email(email)
                    .ville((String) data.get("ville"))
                    .telephone((String) data.get("telephone"))
                    .genre((Integer) data.get("genre"))
                    .dateNaissance(dateNaissance)
                    .build();

            personneRepository.save(personne);

            // Création du candidat
            Candidat candidat = Candidat.builder()
                    .personne(personne)
                    .description((String) data.get("description"))
                    .build();

            String baseUpload = "uploads";

            // --- Gestion des fichiers ---
            MultipartFile imageFile = (MultipartFile) data.get("image_file");
            if (imageFile != null && !imageFile.isEmpty()) {
                String imagePath = saveFile(imageFile, baseUpload + "/images/" + nom + prenom);
                personne.setImage(imagePath);
            }

            MultipartFile cvFile = (MultipartFile) data.get("cv_file");
            if (cvFile != null && !cvFile.isEmpty()) {
                saveFile(cvFile, baseUpload + "/cv/" + nom + prenom);
            }

            // Diplômes
            List<MultipartFile> diplomeFiles = (List<MultipartFile>) data.get("diplome_files");
            if (diplomeFiles != null) {
                for (MultipartFile file : diplomeFiles) {
                    saveFile(file, baseUpload + "/diplomes/" + nom + prenom);
                }
            }

            // Autres fichiers
            List<MultipartFile> autresFiles = (List<MultipartFile>) data.get("autres_files");
            if (autresFiles != null) {
                for (MultipartFile file : autresFiles) {
                    saveFile(file, baseUpload + "/autres/" + nom + prenom);
                }
            }

            // --- Compétences ---
            List<Integer> competencesIds = (List<Integer>) data.get("competences");
            if (competencesIds != null) {
                List<Competence> comps = new java.util.ArrayList<>();
                for (Integer id : competencesIds) {
                    competenceRepository.findById(id).ifPresent(comps::add);
                }
                candidat.setCompetences(comps);
            }

            // --- Langues ---
            List<Integer> languesIds = (List<Integer>) data.get("langues");
            if (languesIds != null) {
                List<Langue> langs = new java.util.ArrayList<>();
                for (Integer id : languesIds) {
                    langueRepository.findById(id).ifPresent(langs::add);
                }
                candidat.setLangues(langs);
            }

            // --- Diplômes filières ---
            List<Integer> diplomeIds = (List<Integer>) data.get("diplomes");
            if (diplomeIds != null) {
                List<DiplomeFiliere> diplomes = new java.util.ArrayList<>();
                for (Integer id : diplomeIds) {
                    diplomeFiliereRepository.findById(id).ifPresent(diplomes::add);
                }
                candidat.setDiplomeFilieres(diplomes);
            }

            // --- Expériences ---
            Map<String, Integer> experiences = (Map<String, Integer>) data.get("experiences");
            if (experiences != null) {
                List<Experience> experienceList = new java.util.ArrayList<>();
                for (Map.Entry<String, Integer> entry : experiences.entrySet()) {
                    Experience exp = new Experience();
                    exp.setNbAnnee(entry.getValue());
                    metierRepository.findById(Integer.parseInt(entry.getKey())).ifPresent(exp::setMetier);
                    exp.setCandidat(candidat);
                    if (exp.getMetier() != null) {
                        experienceList.add(exp);
                    }
                }
                candidat.setExperiences(experienceList);
            }

            candidat.setCandidatures(null);
            candidatRepository.save(candidat);

        } catch (MyException m) {
            throw m;
        } catch (Exception e) {
            throw new MyException("Erreur lors de la création du candidat", e);
        }
    }

    private String saveFile(MultipartFile file, String folderPath) throws MyException {
        try {
            Path folder = Paths.get(folderPath);
            if (!Files.exists(folder)) {
                Files.createDirectories(folder);
            }
            String filePath = folderPath + "/" + file.getOriginalFilename();
            file.transferTo(new File(filePath));
            return filePath;
        } catch (Exception e) {
            throw new MyException("Erreur lors de la sauvegarde du fichier : " + file.getOriginalFilename(), e);
        }
    }

    public List<Candidat> listerCandidats() {
        return candidatRepository.findAll();
    }

    public Candidat getCandidatById(Integer id) throws MyException {
        return candidatRepository.findById(id)
                .orElseThrow(() -> new MyException("Candidat introuvable avec id=" + id));
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
