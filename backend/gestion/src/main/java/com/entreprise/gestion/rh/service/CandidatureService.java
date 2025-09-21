package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.exception.MyException;
import com.entreprise.gestion.rh.model.Besoin;
import com.entreprise.gestion.rh.model.Candidat;
import com.entreprise.gestion.rh.model.Candidature;
import com.entreprise.gestion.rh.repository.BesoinRepository;
import com.entreprise.gestion.rh.repository.CandidatRepository;
import com.entreprise.gestion.rh.repository.CandidatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CandidatureService {

    private final CandidatRepository candidatRepository;
    private final BesoinRepository besoinRepository;
    private final CandidatureRepository candidatureRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void candidater(String mail, Integer idBesoin) throws MyException {
        try {
            // Vérification *
            Candidat candidat = candidatRepository.findByEmail(mail)
                    .orElseThrow(() -> new MyException("Candidat introuvable avec email=" + mail));

            // Vérification besoin
            Besoin besoin = besoinRepository.findById(idBesoin)
                    .orElseThrow(() -> new MyException("Besoin introuvable avec id=" + idBesoin));

            // Vérifier si le candidat a déjà postulé
            boolean dejaCandidat = candidatureRepository.existsByCandidatIdAndBesoinId(candidat.getId(), idBesoin);
            if (dejaCandidat) {
                throw new MyException("Vous avez déjà postulé à ce poste");
            }

            // Créer la candidature
            Candidature candidature = Candidature.builder()
                    .candidat(candidat)
                    .besoin(besoin)
                    .dateCandidature(LocalDateTime.now())
                    .statut(0) // 0 = en attente (par exemple)
                    .build();

            candidatureRepository.save(candidature);

            String notifMessage = "Nouveau candidat pour le poste : " + besoin.getMetier().getLibelle() +
                                  " (Candidat : " + candidat.getPersonne().getNom() + " " +
                                  candidat.getPersonne().getPrenom() + ")";
            messagingTemplate.convertAndSend("/topic/notifications", notifMessage);

        } catch (MyException e) {
            throw e;
        } catch (Exception e) {
            throw new MyException("Erreur lors de la candidature", e);
        }
    }
}
