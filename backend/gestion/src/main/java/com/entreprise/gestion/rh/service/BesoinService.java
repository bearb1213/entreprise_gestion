package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.exception.MyException;
import com.entreprise.gestion.rh.dto.BesoinDTO;
import com.entreprise.gestion.rh.model.Besoin;
import com.entreprise.gestion.rh.model.BesoinCompetence;
import com.entreprise.gestion.rh.model.BesoinLangue;
import com.entreprise.gestion.rh.model.BesoinDiplomeFiliere;
import com.entreprise.gestion.rh.model.Metier;
import com.entreprise.gestion.rh.model.Departement;
import com.entreprise.gestion.rh.model.Competence;
import com.entreprise.gestion.rh.model.Langue;
import com.entreprise.gestion.rh.model.DiplomeFiliere;
import com.entreprise.gestion.rh.repository.BesoinRepository;
import com.entreprise.gestion.rh.repository.MetierRepository;
import com.entreprise.gestion.rh.repository.DepartementRepository;
import com.entreprise.gestion.rh.repository.CompetenceRepository;
import com.entreprise.gestion.rh.repository.LangueRepository;
import com.entreprise.gestion.rh.repository.DiplomeFiliereRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BesoinService {

    private final BesoinRepository besoinRepository;
    private final UtilisateurService utilisateurService;
    private final MetierRepository metierRepository;
    private final DepartementRepository departementRepository;
    private final CompetenceRepository competenceRepository;
    private final LangueRepository langueRepository;
    private final DiplomeFiliereRepository diplomeFiliereRepository;

    // ========== NOUVELLES MÉTHODES POUR LE DÉPARTEMENT DE L'UTILISATEUR ==========

    /**
     * Récupère les besoins du département de l'utilisateur connecté
     */
    public List<BesoinDTO> getBesoinsMonDepartement() throws MyException {
        Integer departementId = getDepartementIdFromAuthentication();
        return besoinRepository.findByDepartementId(departementId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les besoins actifs du département de l'utilisateur connecté
     */
    /**
     * Récupère les besoins actifs du département de l'utilisateur connecté
     */
    public List<BesoinDTO> getBesoinsActifsMonDepartement() throws MyException {
        Integer departementId = getDepartementIdFromAuthentication();
        return besoinRepository.findByDepartementId(departementId).stream()
                .filter(besoin -> besoin.getStatut() == 1) // Filtrage en mémoire pour statut = 1
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupère les besoins avec filtrage par statut pour le département de l'utilisateur
     */
    public List<BesoinDTO> getBesoinsMonDepartementByStatut(Integer statut) throws MyException {
        Integer departementId = getDepartementIdFromAuthentication();
        
        if (statut != null) {
            return besoinRepository.findByDepartementId(departementId).stream()
                    .filter(besoin -> besoin.getStatut().equals(statut)) // Filtrage en mémoire
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } else {
            return besoinRepository.findByDepartementId(departementId).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Méthode utilitaire pour extraire l'ID du département de l'authentification
     */
    private Integer getDepartementIdFromAuthentication() throws MyException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new MyException("Utilisateur non authentifié");
        }

        String username = authentication.getName();
        
        try {
            Map<String, Object> userInfo = utilisateurService.getUtilisateurInfo(username);
            
            if (userInfo.containsKey("departement")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> deptMap = (Map<String, Object>) userInfo.get("departement");
                if (deptMap != null && deptMap.containsKey("id")) {
                    return (Integer) deptMap.get("id");
                }
            }
            
            throw new MyException("L'utilisateur n'est associé à aucun département");
            
        } catch (MyException e) {
            throw e;
        } catch (Exception e) {
            throw new MyException("Erreur lors de la récupération du département: " + e.getMessage());
        }
    }

    // ========== MÉTHODES EXISTANTES ==========

    public List<BesoinDTO> getAllBesoins() {
        return besoinRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BesoinDTO getBesoinById(Integer id) {
        return besoinRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public List<BesoinDTO> getBesoinsActifs() {
        return besoinRepository.findByStatut(1).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BesoinDTO> getBesoinsByMetier(Integer metierId) {
        return besoinRepository.findByMetierId(metierId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BesoinDTO> getBesoinsByDepartement(Integer departementId) {
        return besoinRepository.findByDepartementId(departementId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BesoinDTO createBesoin(BesoinDTO besoinDTO) {
        try {
            // Normaliser les données du frontend
            normalizeBesoinDTO(besoinDTO);

            // Récupérer l'authentification
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            // Vérifier si l'utilisateur a le rôle DEPARTEMENT
            boolean isDepartementUser = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_DEPARTEMENT"));
            
            // Si c'est un utilisateur DEPARTEMENT, forcer l'ID du département
            if (isDepartementUser) {
                Integer userDepartementId = getDepartementIdFromAuthentication();
                
                // Créer un nouveau DepartementDTO avec l'ID de l'utilisateur
                BesoinDTO.DepartementDTO userDeptDTO = new BesoinDTO.DepartementDTO();
                userDeptDTO.setId(userDepartementId);
                
                // Remplacer le département dans le DTO
                besoinDTO.setDepartement(userDeptDTO);
            }
            
            // Validation : s'assurer qu'un département est défini
            if (besoinDTO.getDepartement() == null || besoinDTO.getDepartement().getId() == null) {
                throw new RuntimeException("Le département est obligatoire");
            }
            
            Besoin besoin = convertToEntity(besoinDTO);
            Besoin savedBesoin = besoinRepository.save(besoin);
            return convertToDTO(savedBesoin);
            
        } catch (MyException e) {
            throw new RuntimeException("Erreur lors de la récupération du département: " + e.getMessage());
        }
    }

    /**
     * Normalise les données du DTO en convertissant le format frontend vers le format backend
     */
    private void normalizeBesoinDTO(BesoinDTO dto) {
        // Convertir competences -> besoinCompetences
        if (dto.getCompetences() != null && !dto.getCompetences().isEmpty()) {
            List<BesoinDTO.BesoinCompetenceDTO> besoinCompetences = dto.getCompetences().stream()
                    .map(comp -> {
                        BesoinDTO.BesoinCompetenceDTO bc = new BesoinDTO.BesoinCompetenceDTO();
                        bc.setCoeff(comp.getCoeff() != null ? comp.getCoeff() : 1);
                        BesoinDTO.CompetenceDTO competenceDTO = new BesoinDTO.CompetenceDTO();
                        competenceDTO.setId(comp.getId());
                        bc.setCompetence(competenceDTO);
                        return bc;
                    })
                    .collect(Collectors.toList());
            dto.setBesoinCompetences(besoinCompetences);
        }

        // Convertir langues -> besoinLangues
        if (dto.getLangues() != null && !dto.getLangues().isEmpty()) {
            List<BesoinDTO.BesoinLangueDTO> besoinLangues = dto.getLangues().stream()
                    .map(lang -> {
                        BesoinDTO.BesoinLangueDTO bl = new BesoinDTO.BesoinLangueDTO();
                        bl.setCoeff(lang.getCoeff() != null ? lang.getCoeff() : 1);
                        BesoinDTO.LangueDTO langueDTO = new BesoinDTO.LangueDTO();
                        langueDTO.setId(lang.getId());
                        bl.setLangue(langueDTO);
                        return bl;
                    })
                    .collect(Collectors.toList());
            dto.setBesoinLangues(besoinLangues);
        }

        // Convertir diplomeFilieres -> besoinDiplomeFilieres
        if (dto.getDiplomeFilieres() != null && !dto.getDiplomeFilieres().isEmpty()) {
            List<BesoinDTO.BesoinDiplomeFiliereDTO> besoinDiplomeFilieres = dto.getDiplomeFilieres().stream()
                    .map(diplome -> {
                        BesoinDTO.BesoinDiplomeFiliereDTO bdf = new BesoinDTO.BesoinDiplomeFiliereDTO();

                        if (diplome instanceof Integer) {
                            // Si c'est juste un ID
                            bdf.setCoeff(1); // Coefficient par défaut
                            BesoinDTO.DiplomeFiliereDTO dfDTO = new BesoinDTO.DiplomeFiliereDTO();
                            dfDTO.setId((Integer) diplome);
                            bdf.setDiplomeFiliere(dfDTO);
                        } else if (diplome instanceof java.util.Map) {
                            // Si c'est un objet avec id et coeff
                            @SuppressWarnings("unchecked")
                            java.util.Map<String, Object> diplomeMap = (java.util.Map<String, Object>) diplome;
                            Integer id = (Integer) diplomeMap.get("id");
                            Integer coeff = diplomeMap.get("coeff") != null ? (Integer) diplomeMap.get("coeff") : 1;

                            bdf.setCoeff(coeff);
                            BesoinDTO.DiplomeFiliereDTO dfDTO = new BesoinDTO.DiplomeFiliereDTO();
                            dfDTO.setId(id);
                            bdf.setDiplomeFiliere(dfDTO);
                        }

                        return bdf;
                    })
                    .collect(Collectors.toList());
            dto.setBesoinDiplomeFilieres(besoinDiplomeFilieres);
        }
    }

    public BesoinDTO updateBesoin(Integer id, BesoinDTO besoinDTO) {
        // Normaliser les données du frontend
        normalizeBesoinDTO(besoinDTO);

        return besoinRepository.findById(id)
                .map(existingBesoin -> {
                    updateEntityFromDTO(existingBesoin, besoinDTO);
                    Besoin updatedBesoin = besoinRepository.save(existingBesoin);
                    return convertToDTO(updatedBesoin);
                })
                .orElse(null);
    }

    public boolean deleteBesoin(Integer id) {
        if (besoinRepository.existsById(id)) {
            besoinRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public BesoinDTO updateStatut(Integer id, Integer statut) {
        return besoinRepository.findById(id)
                .map(besoin -> {
                    besoin.setStatut(statut);
                    Besoin updatedBesoin = besoinRepository.save(besoin);
                    return convertToDTO(updatedBesoin);
                })
                .orElse(null);
    }

    public List<BesoinDTO> searchBesoins(Integer metierId, Integer departementId, Integer statut, Integer minAge, Integer maxAge) {
        // Implémentez la logique de recherche selon vos besoins
        return besoinRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ========== MÉTHODES DE CONVERSION ==========

    private BesoinDTO convertToDTO(Besoin besoin) {
        BesoinDTO dto = new BesoinDTO();
        dto.setId(besoin.getId());
        dto.setStatut(besoin.getStatut());
        dto.setMinAge(besoin.getMinAge());
        dto.setNbPosteDispo(besoin.getNbPosteDispo());
        dto.setCoeffAge(besoin.getCoeffAge());
        dto.setCoeffExperience(besoin.getCoeffExperience());
        dto.setMaxAge(besoin.getMaxAge());
        dto.setMinExperience(besoin.getMinExperience());
        
        // Convertir les relations
        if (besoin.getMetier() != null) {
            dto.setMetier(new BesoinDTO.MetierDTO(
                besoin.getMetier().getId(),
                besoin.getMetier().getLibelle()
            ));
        }
        
        if (besoin.getDepartement() != null) {
            dto.setDepartement(new BesoinDTO.DepartementDTO(
                besoin.getDepartement().getId(),
                besoin.getDepartement().getLibelle()
            ));
        }
        
        // Convertir les compétences
        if (besoin.getBesoinCompetences() != null) {
            List<BesoinDTO.BesoinCompetenceDTO> besoinCompetenceDTOs = besoin.getBesoinCompetences()
                    .stream()
                    .map(this::convertBesoinCompetenceToDTO)
                    .collect(Collectors.toList());
            dto.setBesoinCompetences(besoinCompetenceDTOs);
        }
        
        // Convertir les langues
        if (besoin.getBesoinLangues() != null) {
            List<BesoinDTO.BesoinLangueDTO> besoinLangueDTOs = besoin.getBesoinLangues()
                    .stream()
                    .map(this::convertBesoinLangueToDTO)
                    .collect(Collectors.toList());
            dto.setBesoinLangues(besoinLangueDTOs);
        }
        
        // Convertir les diplômes et filières
        if (besoin.getBesoinDiplomeFilieres() != null) {
            List<BesoinDTO.BesoinDiplomeFiliereDTO> besoinDiplomeFiliereDTOs = besoin.getBesoinDiplomeFilieres()
                    .stream()
                    .map(this::convertBesoinDiplomeFiliereToDTO)
                    .collect(Collectors.toList());
            dto.setBesoinDiplomeFilieres(besoinDiplomeFiliereDTOs);
        }
        
        return dto;
    }

    private BesoinDTO.BesoinCompetenceDTO convertBesoinCompetenceToDTO(BesoinCompetence besoinCompetence) {
        BesoinDTO.BesoinCompetenceDTO dto = new BesoinDTO.BesoinCompetenceDTO();
        dto.setId(besoinCompetence.getId());
        dto.setCoeff(besoinCompetence.getCoeff());
        
        if (besoinCompetence.getCompetence() != null) {
            dto.setCompetence(new BesoinDTO.CompetenceDTO(
                besoinCompetence.getCompetence().getId(),
                besoinCompetence.getCompetence().getLibelle()
            ));
        }
        
        return dto;
    }

    private BesoinDTO.BesoinLangueDTO convertBesoinLangueToDTO(BesoinLangue besoinLangue) {
        BesoinDTO.BesoinLangueDTO dto = new BesoinDTO.BesoinLangueDTO();
        dto.setId(besoinLangue.getId());
        dto.setCoeff(besoinLangue.getCoeff());
        
        if (besoinLangue.getLangue() != null) {
            dto.setLangue(new BesoinDTO.LangueDTO(
                besoinLangue.getLangue().getId(),
                besoinLangue.getLangue().getLibelle()
            ));
        }
        
        return dto;
    }

    private BesoinDTO.BesoinDiplomeFiliereDTO convertBesoinDiplomeFiliereToDTO(BesoinDiplomeFiliere besoinDiplomeFiliere) {
        BesoinDTO.BesoinDiplomeFiliereDTO dto = new BesoinDTO.BesoinDiplomeFiliereDTO();
        dto.setId(besoinDiplomeFiliere.getId());
        dto.setCoeff(besoinDiplomeFiliere.getCoeff());
        
        if (besoinDiplomeFiliere.getDiplomeFiliere() != null) {
            DiplomeFiliere diplomeFiliere = besoinDiplomeFiliere.getDiplomeFiliere();
            
            BesoinDTO.DiplomeDTO diplomeDTO = null;
            if (diplomeFiliere.getDiplome() != null) {
                diplomeDTO = new BesoinDTO.DiplomeDTO(
                    diplomeFiliere.getDiplome().getId(),
                    diplomeFiliere.getDiplome().getLibelle()
                );
            }
            
            BesoinDTO.FiliereDTO filiereDTO = null;
            if (diplomeFiliere.getFiliere() != null) {
                filiereDTO = new BesoinDTO.FiliereDTO(
                    diplomeFiliere.getFiliere().getId(),
                    diplomeFiliere.getFiliere().getLibelle()
                );
            }
            
            dto.setDiplomeFiliere(new BesoinDTO.DiplomeFiliereDTO(
                diplomeFiliere.getId(),
                diplomeDTO,
                filiereDTO
            ));
        }
        
        return dto;
    }

    private Besoin convertToEntity(BesoinDTO dto) {
        Besoin besoin = new Besoin();
        updateEntityFromDTO(besoin, dto);
        return besoin;
    }

    private void updateEntityFromDTO(Besoin besoin, BesoinDTO dto) {
        besoin.setStatut(dto.getStatut());
        besoin.setMinAge(dto.getMinAge());
        besoin.setNbPosteDispo(dto.getNbPosteDispo());
        besoin.setCoeffAge(dto.getCoeffAge());
        besoin.setCoeffExperience(dto.getCoeffExperience());
        besoin.setMaxAge(dto.getMaxAge());
        besoin.setMinExperience(dto.getMinExperience());

        // --- Métier
        if (dto.getMetier() != null && dto.getMetier().getId() != null) {
            Metier metier = metierRepository.findById(dto.getMetier().getId())
                    .orElseThrow(() -> new RuntimeException("Métier non trouvé"));
            besoin.setMetier(metier);
        }

        // --- Département
        if (dto.getDepartement() != null && dto.getDepartement().getId() != null) {
            Departement departement = departementRepository.findById(dto.getDepartement().getId())
                    .orElseThrow(() -> new RuntimeException("Département non trouvé"));
            besoin.setDepartement(departement);
        }

        // --- Compétences
        if (dto.getBesoinCompetences() != null) {
            List<BesoinCompetence> besoinCompetences = dto.getBesoinCompetences().stream()
                    .map(comp -> {
                        BesoinCompetence bc = new BesoinCompetence();
                        bc.setBesoin(besoin);
                        bc.setCoeff(comp.getCoeff());
                        if (comp.getCompetence() != null && comp.getCompetence().getId() != null) {
                            Competence competence = competenceRepository.findById(comp.getCompetence().getId())
                                    .orElseThrow(() -> new RuntimeException("Compétence non trouvée"));
                            bc.setCompetence(competence);
                        }
                        return bc;
                    })
                    .collect(Collectors.toList());
            besoin.setBesoinCompetences(besoinCompetences);
        }

        // --- Langues
        if (dto.getBesoinLangues() != null) {
            List<BesoinLangue> besoinLangues = dto.getBesoinLangues().stream()
                    .map(lang -> {
                        BesoinLangue bl = new BesoinLangue();
                        bl.setBesoin(besoin);
                        bl.setCoeff(lang.getCoeff());
                        if (lang.getLangue() != null && lang.getLangue().getId() != null) {
                            Langue langue = langueRepository.findById(lang.getLangue().getId())
                                    .orElseThrow(() -> new RuntimeException("Langue non trouvée"));
                            bl.setLangue(langue);
                        }
                        return bl;
                    })
                    .collect(Collectors.toList());
            besoin.setBesoinLangues(besoinLangues);
        }

        // --- Diplômes / Filières
        if (dto.getBesoinDiplomeFilieres() != null) {
            List<BesoinDiplomeFiliere> besoinDiplomeFilieres = dto.getBesoinDiplomeFilieres().stream()
                    .map(diplome -> {
                        BesoinDiplomeFiliere bdf = new BesoinDiplomeFiliere();
                        bdf.setBesoin(besoin);
                        bdf.setCoeff(diplome.getCoeff());
                        if (diplome.getDiplomeFiliere() != null && diplome.getDiplomeFiliere().getId() != null) {
                            DiplomeFiliere diplomeFiliere = diplomeFiliereRepository.findById(diplome.getDiplomeFiliere().getId())
                                    .orElseThrow(() -> new RuntimeException("DiplomeFiliere non trouvé"));
                            bdf.setDiplomeFiliere(diplomeFiliere);
                        }
                        return bdf;
                    })
                    .collect(Collectors.toList());
            besoin.setBesoinDiplomeFilieres(besoinDiplomeFilieres);
        }
    }

}

