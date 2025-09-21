package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.rh.model.Utilisateur;
import com.entreprise.gestion.rh.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashMap;
import org.springframework.http.ResponseEntity;
import com.entreprise.gestion.exception.MyException;


@RestController
@RequestMapping("/api/public/utilisateur")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Utilisateur credentials, HttpSession session) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getLogin(), credentials.getMdp())
            );
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            // Sauvegarder le SecurityContext dans la session
            session.setAttribute("SPRING_SECURITY_CONTEXT", context);

            Map<String, Object> utilisateurInfo = utilisateurService.getUtilisateurInfo(credentials.getLogin());
            session.setAttribute("auth", utilisateurInfo);
            return utilisateurInfo;

        } catch (AuthenticationException e) {
            throw new MyException("Identifiants incorrects",e);
        }
    }

    /**
     * ✨ me
     * Description :
     *   Récupère les informations de l’utilisateur actuellement connecté.
     *   Accessible uniquement aux utilisateurs authentifiés.
     *
     * Input :
     *   - Authentication authentication : objet fourni par Spring contenant les détails de l’utilisateur connecté.
     *
     * Retour :
     *   - Map<String, Object> : contient le login et les rôles de l’utilisateur connecté.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        return Map.of(
                "login", authentication.getName(),
                "roles", authentication.getAuthorities()
        );
    }

    /**
     * ✨ logout
     * Description :
     *   Déconnecte l’utilisateur en invalidant la session et en nettoyant le SecurityContext.
     *   Accessible uniquement aux utilisateurs authentifiés.
     *
     * Input :
     *   - HttpSession session : session HTTP à invalider.
     *
     * Retour :
     *   - ResponseEntity<Map<String, String>> :
     *       - 200 OK avec message "Déconnexion réussie" si succès
     *       - 500 INTERNAL_SERVER_ERROR avec message d’erreur si problème lors de la déconnexion
     */
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