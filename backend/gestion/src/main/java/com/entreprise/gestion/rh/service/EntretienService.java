package com.entreprise.gestion.rh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entreprise.gestion.rh.model.Candidat;
import com.entreprise.gestion.rh.model.Entretien;
import com.entreprise.gestion.rh.repository.EntretienRepository;

@Service
public class EntretienService {
    
    @Autowired 
    private EntretienRepository entretienRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public Entretien saveEntretien(Entretien entretien) throws Exception
    {
        sendMailEntretien(entretien);
        return entretienRepository.save(entretien);
    }

    public void sendMailEntretien(Entretien entretien) throws Exception
    {
        Candidat candidat = entretien.getCandidature().getCandidat();
        String to = candidat.getPersonne().getEmail();
        String nomCandidat = candidat.getPersonne().getNom()+" "+candidat.getPersonne().getPrenom();
        String objet = "Entretien - Système RH";
        String dateEntretien = entretien.getDateHeureDebut().toLocalDate().toString();
        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: #007bff; color: white; padding: 20px; text-align: center; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1> Candidature retenue </h1>
                    </div>
                    <div>
                        <h2>Bonjour, %s !</h2>
                        <p>Felicitations, votre dossier a été selectionné pour la phase d'entretien</p>
                        <p>Nous vous prions de vous rendre à nos locaux le %s. à nos heures d'ouverture habituelles</p>
                        <p>Cordialement.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(nomCandidat,dateEntretien);
        
        emailService.sendHtmlEmail(to, objet, htmlContent);
    }
}
