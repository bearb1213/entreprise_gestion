package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.dto.BesoinDTO;
import com.entreprise.gestion.rh.model.Besoin;
import com.entreprise.gestion.rh.repository.BesoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BesoinService {

    @Autowired
    private BesoinRepository besoinRepository;

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

    public BesoinDTO createBesoin(BesoinDTO besoinDTO) {
        Besoin besoin = convertToEntity(besoinDTO);
        Besoin savedBesoin = besoinRepository.save(besoin);
        return convertToDTO(savedBesoin);
    }

    public BesoinDTO updateBesoin(Integer id, BesoinDTO besoinDTO) {
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
        return besoinRepository.findAll().stream() // Remplacez par votre méthode de repository
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private BesoinDTO convertToDTO(Besoin besoin) {
        BesoinDTO dto = new BesoinDTO();
        dto.setId(besoin.getId());
        dto.setStatut(besoin.getStatut());
        dto.setMinAge(besoin.getMinAge());
        dto.setNbPosteDsipo(besoin.getNbPosteDsipo());
        dto.setCoeffAge(besoin.getCoeffAge());
        dto.setCoeffExperience(besoin.getCoeffExperience());
        dto.setMaxAge(besoin.getMaxAge());
        dto.setMinExperience(besoin.getMinExperience());
        
        // Convertir les relations (à compléter selon vos besoins)
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
        besoin.setNbPosteDsipo(dto.getNbPosteDsipo());
        besoin.setCoeffAge(dto.getCoeffAge());
        besoin.setCoeffExperience(dto.getCoeffExperience());
        besoin.setMaxAge(dto.getMaxAge());
        besoin.setMinExperience(dto.getMinExperience());
        // Gérer les relations selon vos besoins
    }
}