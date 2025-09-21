package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.model.Utilisateur;
import com.entreprise.gestion.rh.repository.UtilisateurRepository;
import com.entreprise.gestion.exception.MyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
}
