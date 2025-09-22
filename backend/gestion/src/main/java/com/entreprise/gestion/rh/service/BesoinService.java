package com.entreprise.gestion.rh.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entreprise.gestion.rh.dto.BesoinCriteria;
import com.entreprise.gestion.rh.dto.BesoinDTO;
import com.entreprise.gestion.rh.dto.BesoinRequest;
import com.entreprise.gestion.rh.dto.IdWithCoeff;
import com.entreprise.gestion.rh.model.Besoin;
import com.entreprise.gestion.rh.model.BesoinCompetence;
import com.entreprise.gestion.rh.model.BesoinDiplomeFiliere;
import com.entreprise.gestion.rh.model.BesoinLangue;
import com.entreprise.gestion.rh.model.Competence;
import com.entreprise.gestion.rh.model.DiplomeFiliere;
import com.entreprise.gestion.rh.model.Langue;
import com.entreprise.gestion.rh.repository.BesoinRepository;
import com.entreprise.gestion.rh.repository.CompetenceRepository;
import com.entreprise.gestion.rh.repository.DepartementRepository;
import com.entreprise.gestion.rh.repository.DiplomeFiliereRepository;
import com.entreprise.gestion.rh.repository.LangueRepository;
import com.entreprise.gestion.rh.repository.MetierRepository;

import jakarta.transaction.Transactional;

@Service
public class BesoinService {
    @Autowired
    private BesoinRepository besoinRepository;
    
    @Autowired
    private MetierRepository metierRepository;
    
    @Autowired
    private DepartementRepository departementRepository;
    
    @Autowired
    private CompetenceRepository competenceRepository;
    
    @Autowired
    private LangueRepository langueRepository;
    
    @Autowired
    private DiplomeFiliereRepository diplomeFiliereRepository;

    public List<Besoin> findAll() { return besoinRepository.findAll(); }
    public Optional<Besoin> findById(Integer id) { return besoinRepository.findById(id); }
    public Besoin save(Besoin besoin) { return besoinRepository.save(besoin); }

    public List<BesoinDTO> getAllBesoins() {
        return besoinRepository.findAll().stream()
            .map(b -> new BesoinDTO(
                b.getId(),
                b.getStatut(),
                b.getMinAge(),
                b.getMaxAge(),
                b.getNbPosteDispo(),
                b.getMinExperience(),
                b.getCoeffAge(),
                b.getCoeffExperience(),
                b.getMetier() != null ? b.getMetier().getLibelle() : null,
                b.getDepartement() != null ? b.getDepartement().getLibelle() : null,
                b.getBesoinCompetences().stream()
                    .map(bc -> bc.getCompetence().getLibelle())
                    .toList(),
                b.getBesoinLangues().stream()
                    .map(bl -> bl.getLangue().getLibelle())
                    .toList(),
                b.getBesoinDiplomeFilieres().stream()
                    .map(bd -> bd.getDiplomeFiliere().getDiplome().getLibelle() 
                            + " - " + bd.getDiplomeFiliere().getFiliere().getLibelle())
                    .toList()
            ))
            .toList();
    }

    @Transactional
    public Besoin createBesoin(BesoinRequest request) {
        Besoin besoin = new Besoin();
        besoin.setStatut(request.getStatut());
        besoin.setMinAge(request.getMinAge());
        besoin.setMaxAge(request.getMaxAge());
        besoin.setNbPosteDispo(request.getNbPosteDispo());
        besoin.setMinExperience(request.getMinExperience());
        besoin.setCoeffAge(request.getCoeffAge());
        besoin.setCoeffExperience(request.getCoeffExperience());

        besoin.setMetier(metierRepository.findById(request.getMetierId())
                .orElseThrow(() -> new RuntimeException("Metier non trouvé")));
        besoin.setDepartement(departementRepository.findById(request.getDepartementId())
                .orElseThrow(() -> new RuntimeException("Departement non trouvé")));

        besoin = besoinRepository.save(besoin);

        if (request.getCompetences() != null) {
            for (IdWithCoeff compDto : request.getCompetences()) {
                Competence comp = competenceRepository.findById(compDto.getId())
                        .orElseThrow(() -> new RuntimeException("Compétence non trouvée"));
                BesoinCompetence bc = new BesoinCompetence();
                bc.setBesoin(besoin);
                bc.setCompetence(comp);
                bc.setCoeff(compDto.getCoeff()); 
                besoin.getBesoinCompetences().add(bc);
            }
        }

        if (request.getLangues() != null) {
            for (IdWithCoeff langDto : request.getLangues()) {
                Langue lang = langueRepository.findById(langDto.getId())
                        .orElseThrow(() -> new RuntimeException("Langue non trouvée"));
                BesoinLangue bl = new BesoinLangue();
                bl.setBesoin(besoin);
                bl.setLangue(lang);
                bl.setCoeff(langDto.getCoeff()); 
                besoin.getBesoinLangues().add(bl);
            }
        }

        if (request.getDiplomeFilieres() != null) {
            for (IdWithCoeff dfDto : request.getDiplomeFilieres()) {
                DiplomeFiliere df = diplomeFiliereRepository.findById(dfDto.getId())
                        .orElseThrow(() -> new RuntimeException("Diplome-Filiere non trouvé"));
                BesoinDiplomeFiliere bd = new BesoinDiplomeFiliere();
                bd.setBesoin(besoin);
                bd.setDiplomeFiliere(df);
                bd.setCoeff(dfDto.getCoeff()); 
                besoin.getBesoinDiplomeFilieres().add(bd);
            }
        }

        return besoinRepository.save(besoin);
    }

     public List<Besoin> filterBesoins(BesoinCriteria criteria) {
        return besoinRepository.findAll().stream()

                .filter(b -> criteria.getStatut() == null || 
                             (b.getStatut() != null && b.getStatut().equals(criteria.getStatut())))

                .filter(b -> criteria.getMinAge() == null || b.getMinAge() >= criteria.getMinAge())
                .filter(b -> criteria.getMaxAge() == null || b.getMaxAge() <= criteria.getMaxAge())

                .filter(b -> criteria.getMinExperience() == null || b.getMinExperience() >= criteria.getMinExperience())

                .filter(b -> criteria.getDepartementId() == null || 
                             (b.getDepartement() != null && b.getDepartement().getId().equals(criteria.getDepartementId())))

                .filter(b -> criteria.getMetierId() == null || 
                             (b.getMetier() != null && b.getMetier().getId().equals(criteria.getMetierId())))

                .filter(b -> criteria.getCompetenceIds() == null || criteria.getCompetenceIds().isEmpty() ||
                             b.getBesoinCompetences().stream()
                                     .map(bc -> bc.getCompetence().getId())
                                     .collect(java.util.stream.Collectors.toSet())
                                     .containsAll(criteria.getCompetenceIds()))

                .filter(b -> criteria.getLangueIds() == null || criteria.getLangueIds().isEmpty() ||
                             b.getBesoinLangues().stream()
                                     .map(bl -> bl.getLangue().getId())
                                     .collect(java.util.stream.Collectors.toSet())
                                     .containsAll(criteria.getLangueIds()))

                .filter(b -> criteria.getDiplomeFiliereIds() == null || criteria.getDiplomeFiliereIds().isEmpty() ||
                             b.getBesoinDiplomeFilieres().stream()
                                     .map(bd -> bd.getDiplomeFiliere().getId())
                                     .collect(java.util.stream.Collectors.toSet())
                                     .containsAll(criteria.getDiplomeFiliereIds()))

                .sorted(Comparator
                        .comparing(Besoin::getCoeffExperience).reversed()
                        .thenComparing(Besoin::getNbPosteDispo).reversed()
                        .thenComparing(Besoin::getMinAge))
                .toList();
    }
}