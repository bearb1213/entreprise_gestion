package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.model.Utilisateur;
import com.entreprise.gestion.rh.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    /**
     * Charge un utilisateur par son login pour Spring Security.
     * Input : login - String correspondant au nom d'utilisateur
     * Output : UserDetails - objet UserDetails utilisé par Spring Security
     * @throws UsernameNotFoundException si l'utilisateur n'existe pas
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        
        // Récupérer l'utilisateur depuis la base
        Utilisateur utilisateur = utilisateurRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        // Déterminer les rôles de l'utilisateur
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        if (utilisateur!= null && utilisateur.getDepartement() != null) {
            String role = utilisateur.getDepartement().getLibelle().toUpperCase(); // ex: ADMIN, FINANCE, AGENT
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        } 

        // Retourner un UserDetails compatible Spring Security
        return User.builder()
                .username(utilisateur.getLogin())
                .password(utilisateur.getMdp()) // mot de passe en clair ou encodé selon ta configuration
                .authorities(authorities)
                .build();
    }
}