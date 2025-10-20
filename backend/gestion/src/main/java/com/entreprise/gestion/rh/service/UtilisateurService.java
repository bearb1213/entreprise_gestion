package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.model.Utilisateur;
import com.entreprise.gestion.rh.repository.UtilisateurRepository;
import com.entreprise.gestion.exception.MyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;

    public Map<String, Object> getUtilisateurInfo(String login) throws MyException {
        try {
            Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                    .orElseThrow(() -> new MyException("❌ Utilisateur introuvable avec le login : " + login));

            Map<String, Object> result = new HashMap<>();
            result.put("id", utilisateur.getId());
            result.put("login", utilisateur.getLogin());

            // Ajout du département
            if (utilisateur.getDepartement() != null) {
                Map<String, Object> deptMap = new HashMap<>();
                deptMap.put("id", utilisateur.getDepartement().getId());
                deptMap.put("nom", utilisateur.getDepartement().getLibelle());
                result.put("departement", deptMap);
            }

            return result;

        } catch (MyException e) {
            throw e;
        } catch (Exception e) {
            throw new MyException("❌ Une erreur est survenue lors de la récupération de l'utilisateur");
        }
    }

    public Map<String, Object> getUtilisateursRH() throws MyException {
        try {
            // Récupérer tous les utilisateurs du département RH
            List<Utilisateur> utilisateursRH = utilisateurRepository.findByDepartementLibelle("Rh");
            
            // Convertir en liste de Maps
            List<Map<String, Object>> utilisateursList = utilisateursRH.stream()
                    .map(utilisateur -> {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("id", utilisateur.getId());
                        userMap.put("login", utilisateur.getLogin());
                        
                        if (utilisateur.getDepartement() != null) {
                            Map<String, Object> deptMap = new HashMap<>();
                            deptMap.put("id", utilisateur.getDepartement().getId());
                            deptMap.put("nom", utilisateur.getDepartement().getLibelle());
                            userMap.put("departement", deptMap);
                        }
                        
                        return userMap;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> result = new HashMap<>();
            result.put("utilisateurs", utilisateursList);
            result.put("total", utilisateursList.size());
            result.put("departement", "Ressources Humaines");

            return result;

        } catch (Exception e) {
            throw new MyException("❌ Erreur lors de la récupération des utilisateurs RH : " + e.getMessage());
        }
    }
}
