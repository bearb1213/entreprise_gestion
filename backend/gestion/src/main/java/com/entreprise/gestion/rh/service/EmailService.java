package com.entreprise.gestion.rh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Email texte simple
    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("noreply@entreprise.com");
        
        mailSender.send(message);
        System.out.println("Email envoyé à " + to);
    }

    // Email HTML
    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom("noreply@entreprise.com");
        
        mailSender.send(message);
    }

    // Email avec pièce jointe
    public void sendEmailWithAttachment(String to, String subject, String text, 
                                      String attachmentPath) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, false); // false = texte simple, pas HTML
        helper.setFrom("noreply@entreprise.com");
        
        // Ajouter pièce jointe
        File file = new File(attachmentPath);
        if (file.exists()) {
            helper.addAttachment(file.getName(), file);
        }
        
        mailSender.send(message);
    }

    public void sendHtmlEmailWithAttachment(String to, String subject, String htmlContent,String fileName,byte[] attachmentData,String contentType) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom("noreply@entreprise.com");
        helper.addAttachment(fileName, new ByteArrayResource(attachmentData),contentType);
        
        mailSender.send(message);
    }

     public void envoyerEmailAvecLien(String to, String nomCandidat, String lienQCM) throws MessagingException {
        String subject = "Invitation à passer le QCM - Votre candidature";
        
        String htmlContent = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <style>\n" +
            "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }\n" +
            "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }\n" +
            "        .header { background: #f8f9fa; padding: 20px; text-align: center; }\n" +
            "        .content { padding: 20px; }\n" +
            "        .button { \n" +
            "            display: inline-block; \n" +
            "            padding: 12px 24px; \n" +
            "            background-color: #007bff; \n" +
            "            color: white; \n" +
            "            text-decoration: none; \n" +
            "            border-radius: 5px; \n" +
            "            margin: 15px 0;\n" +
            "        }\n" +
            "        .footer { \n" +
            "            margin-top: 20px; \n" +
            "            padding: 15px; \n" +
            "            background: #f8f9fa; \n" +
            "            text-align: center; \n" +
            "            font-size: 12px; \n" +
            "            color: #666;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"container\">\n" +
            "        <div class=\"header\">\n" +
            "            <h2>Entreprise RH</h2>\n" +
            "        </div>\n" +
            "        <div class=\"content\">\n" +
            "            <h3>Bonjour " + nomCandidat + ",</h3>\n" +
            "            <p>Nous avons le plaisir de vous inviter à passer notre test de qualification en ligne.</p>\n" +
            "            <p>Veuillez cliquer sur le bouton ci-dessous pour accéder au questionnaire :</p>\n" +
            "            \n" +
            "            <div style=\"text-align: center;\">\n" +
            "                <a href=\"" + lienQCM + "\" class=\"button\">\n" +
            "                    Accéder au QCM\n" +
            "                </a>\n" +
            "            </div>\n" +
            "            \n" +
            "            <p><strong>Informations importantes :</strong></p>\n" +
            "            <ul>\n" +
            "                <li>Durée estimée : 30 minutes</li>\n" +
            "                <li>Assurez-vous d'avoir une connexion internet stable</li>\n" +
            "                <li>Ce lien est personnel et unique</li>\n" +
            "            </ul>\n" +
            "            \n" +
            "            <p>Si le bouton ne fonctionne pas, vous pouvez copier-coller ce lien dans votre navigateur :</p>\n" +
            "            <p style=\"word-break: break-all; color: #007bff;\">" + lienQCM + "</p>\n" +
            "        </div>\n" +
            "        <div class=\"footer\">\n" +
            "            <p>Cet email a été envoyé automatiquement, merci de ne pas y répondre.</p>\n" +
            "            <p>&copy; 2025 Entreprise RH. Tous droits réservés.</p>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";
        
        sendHtmlEmail(to, subject, htmlContent);
    }

    // Autre exemple : Email de confirmation après QCM
    public void envoyerConfirmationQCM(String to, String nomCandidat, Float note, String prochaineEtape) throws MessagingException {
        String subject = "Confirmation de réception - Votre test QCM";
        
        String htmlContent = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <style>\n" +
            "        body { font-family: Arial, sans-serif; }\n" +
            "        .container { max-width: 600px; margin: 0 auto; }\n" +
            "        .success { color: #28a745; }\n" +
            "        .info { background: #d1ecf1; padding: 10px; border-radius: 5px; }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"container\">\n" +
            "        <h2>Confirmation de réception</h2>\n" +
            "        <p>Bonjour <strong>" + nomCandidat + "</strong>,</p>\n" +
            "        \n" +
            "        <p class=\"success\">Nous avons bien reçu vos réponses au test QCM.</p>\n" +
            "        \n" +
            "        <div class=\"info\">\n" +
            "            <p><strong>Note obtenue :</strong> " + note + "/20</p>\n" +
            "            <p><strong>Prochaine étape :</strong> " + prochaineEtape + "</p>\n" +
            "        </div>\n" +
            "        \n" +
            "        <p>Vous serez contacté(e) très prochainement concernant la suite de votre candidature.</p>\n" +
            "        \n" +
            "        <p>Cordialement,<br>L'équipe RH</p>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";
        
        sendHtmlEmail(to, subject, htmlContent);
    }

    // Exemple : Email avec lien de connexion à l'espace candidat
    public void envoyerLienEspaceCandidat(String to, String nomCandidat, String lienEspace, String identifiant) throws MessagingException {
        String subject = "Votre espace candidat - Accès personnel";
        
        String htmlContent = "<html>\n" +
            "<body>\n" +
            "    <h2>Votre espace candidat</h2>\n" +
            "    <p>Bonjour " + nomCandidat + ",</p>\n" +
            "    \n" +
            "    <p>Vous pouvez maintenant accéder à votre espace candidat personnel :</p>\n" +
            "    \n" +
            "    <p><a href=\"" + lienEspace + "\" style=\"color: blue; text-decoration: underline;\">\n" +
            "        Accéder à mon espace candidat\n" +
            "    </a></p>\n" +
            "    \n" +
            "    <p><strong>Identifiant :</strong> " + identifiant + "</p>\n" +
            "    \n" +
            "    <p>Dans cet espace, vous pourrez :</p>\n" +
            "    <ul>\n" +
            "        <li>Suivre l'état de vos candidatures</li>\n" +
            "        <li>Compléter votre profil</li>\n" +
            "        <li>Consulter vos convocations</li>\n" +
            "    </ul>\n" +
            "</body>\n" +
            "</html>";
        
        sendHtmlEmail(to, subject, htmlContent);
    }
}