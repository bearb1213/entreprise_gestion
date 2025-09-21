package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.rh.service.UtilisateurService;
import com.entreprise.gestion.rh.service.PersonneService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.entreprise.gestion.exception.MyException;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetails;
import com.entreprise.gestion.rh.service.CustomUserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.authentication.BadCredentialsException;


@RestController
@RequestMapping("/api/utilisateur")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;
    private final PersonneService personneService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> credentials, HttpSession session) throws Exception {
        try {
            Map<String, Object> authInfo;
            Authentication authentication;

            // 🔹 Cas 1 : login + mdp
            if (credentials.containsKey("login")) {
                String login = credentials.get("login");
                String mdp = credentials.get("mdp");

                if (login == null || login.isBlank() || mdp == null || mdp.isBlank()) {
                    throw new MyException("Login et mot de passe obligatoires");
                }

                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(login, mdp)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                authInfo = utilisateurService.getUtilisateurInfo(login);
            } 
            // 🔹 Cas 2 : email seul
            else if (credentials.containsKey("email")) {
                String email = credentials.get("email");
                if (email == null || email.isBlank()) {
                    throw new MyException("Email obligatoire");
                }

                // Clear contexte
                SecurityContextHolder.clearContext();

                // Charger le UserDetails pour email
                UserDetails userDetails = userDetailsService.loadUserByEmail(email);

                // Créer un token et mettre le principal en email
                authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,  // 🔹 username = email
                        null,                       // mot de passe vide
                        userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                authInfo = new HashMap<>();
                authInfo.put("email", email);

            } else {
                throw new MyException("Données de connexion invalides");
            }

            // Sauvegarder dans session
            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext()
            );
            session.setAttribute("auth", authInfo);

            return authInfo;

        } catch (AuthenticationException e) {
            throw new MyException("Identifiants incorrects", e);
        }
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        System.out.println("Principal: " + authentication.getPrincipal());
        System.out.println("Principal class: " + authentication.getPrincipal().getClass());
        System.out.println("Name: " + authentication.getName());
        System.out.println("Authorities: " + authentication.getAuthorities());

        Object principal = authentication.getPrincipal();
        String login;
        String userType = "unknown";

        if (principal instanceof UserDetails userDetails) {
            login = userDetails.getUsername();
            userType = "UserDetails";
        } else if (principal instanceof String) {
            login = (String) principal;
            userType = "String";
        } else {
            login = authentication.getName();
            userType = "Authentication name";
        }

        return Map.of(
                "login", login,
                "roles", authentication.getAuthorities(),
                "userType", userType,
                "principalClass", principal.getClass().getSimpleName()
        );
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        try {
            session.invalidate();
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok(Map.of("message", "Déconnexion réussie"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur lors de la déconnexion"));
        }
    }
}
