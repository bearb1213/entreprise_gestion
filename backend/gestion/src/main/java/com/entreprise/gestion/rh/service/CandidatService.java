package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.model.Candidat;
import com.entreprise.gestion.rh.model.Competence;
import com.entreprise.gestion.rh.model.DiplomeFiliere;
import com.entreprise.gestion.rh.model.Experience;
import com.entreprise.gestion.rh.model.Langue;
import com.entreprise.gestion.rh.model.Personne;
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
}
