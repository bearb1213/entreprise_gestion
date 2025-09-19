package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.model.Departement;
import com.entreprise.gestion.rh.repository.DepartementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartementService {
    
    @Autowired
    private DepartementRepository departementRepository;
    
    public List<Departement> getAllDepartements() {
        return departementRepository.findAll();
    }
    
    public Optional<Departement> getDepartementById(Integer id) {
        return departementRepository.findById(id);
    }
    
    public Departement saveDepartement(Departement departement) {
        return departementRepository.save(departement);
    }
    
    public Departement updateDepartement(Integer id, Departement departementDetails) {
        Optional<Departement> departement = departementRepository.findById(id);
        if (departement.isPresent()) {
            Departement existingDepartement = departement.get();
            existingDepartement.setLibelle(departementDetails.getLibelle());
            return departementRepository.save(existingDepartement);
        }
        return null;
    }
    
    public void deleteDepartement(Integer id) {
        departementRepository.deleteById(id);
    }
    
}
