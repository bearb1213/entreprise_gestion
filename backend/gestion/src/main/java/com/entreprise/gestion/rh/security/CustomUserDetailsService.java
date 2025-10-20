package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.model.Utilisateur;
import com.entreprise.gestion.rh.model.Personne;
import com.entreprise.gestion.rh.repository.UtilisateurRepository;
import com.entreprise.gestion.rh.repository.PersonneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    /**
     * Méthode standard utilisée par Spring Security.
     * Authentifie un Utilisateur via login + mot de passe.
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        System.out.println("TOKONY ROLE: " + utilisateur.getDepartement().getLibelle());
        if (utilisateur.getDepartement() != null && utilisateur.getDepartement().getLibelle().equals("Admin") || utilisateur.getDepartement().getLibelle().equals("Rh") || utilisateur.getDepartement().getLibelle().equals("Unite")) {
            String role = utilisateur.getDepartement().getLibelle().toUpperCase();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_DEPARTEMENT"));
        }

        return User.builder()
                .username(utilisateur.getLogin())
                .password(utilisateur.getMdp())
                .authorities(authorities)
                .build();
    }

    /**
     * 🔹 Nouvelle méthode : authentification via Personne (email uniquement)
     */
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        // Ici pas de mot de passe, donc on renvoie un user avec "" comme password
        return User.builder()
                .username(email)
                .password("") // mot de passe vide
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_CANDIDAT")))
                .build();
    }
}
