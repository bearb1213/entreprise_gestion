package com.entreprise.gestion.rh.service;

import org.springframework.beans.factory.annotation.Autowired;
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

    // Méthode utilitaire pour créer des emails HTML simples
    public void sendWelcomeEmail(String to, String username) throws MessagingException {
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
                        <h1>Bienvenue !</h1>
                    </div>
                    <div>
                        <h2>Bonjour, %s !</h2>
                        <p>Bienvenue dans notre système de gestion RH.</p>
                        <p>Votre compte a été créé avec succès.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(username);
        
        sendHtmlEmail(to, "Bienvenue - Système RH", htmlContent);
    }
    public void sendConfirmationEmail(String to, String candidateName) throws MessagingException {
        String subject = "Confirmation de réception de votre candidature - Entreprise XYZ";
        
        String htmlContent = """
            <!DOCTYPE html>
            <html lang="fr">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Confirmation de Candidature</title>
                <style>
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        line-height: 1.6;
                        color: #333;
                        margin: 0;
                        padding: 0;
                        background-color: #f9f9f9;
                    }
                    .container {
                        max-width: 600px;
                        margin: 0 auto;
                        background: #ffffff;
                        border-radius: 10px;
                        overflow: hidden;
                        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
                    }
                    .header {
                        background: linear-gradient(135deg, #007bff, #0056b3);
                        color: white;
                        padding: 30px 20px;
                        text-align: center;
                    }
                    .header h1 {
                        margin: 0;
                        font-size: 28px;
                        font-weight: 600;
                    }
                    .content {
                        padding: 40px 30px;
                    }
                    .greeting {
                        font-size: 18px;
                        color: #007bff;
                        margin-bottom: 25px;
                        font-weight: 500;
                    }
                    .message {
                        font-size: 16px;
                        line-height: 1.8;
                        color: #555;
                        margin-bottom: 20px;
                    }
                    .important-info {
                        background: #f8f9fa;
                        border-left: 4px solid #007bff;
                        padding: 20px;
                        margin: 30px 0;
                        border-radius: 4px;
                    }
                    .info-title {
                        font-weight: 600;
                        color: #007bff;
                        margin-bottom: 10px;
                        font-size: 18px;
                    }
                    .info-details {
                        font-size: 16px;
                        color: #495057;
                    }
                    .date-time {
                        font-weight: 600;
                        color: #28a745;
                        font-size: 18px;
                    }
                    .instructions {
                        background: #e7f3ff;
                        border-radius: 8px;
                        padding: 20px;
                        margin: 25px 0;
                    }
                    .instructions-title {
                        font-weight: 600;
                        color: #0056b3;
                        margin-bottom: 15px;
                    }
                    .instructions-list {
                        margin: 0;
                        padding-left: 20px;
                    }
                    .instructions-list li {
                        margin-bottom: 8px;
                    }
                    .footer {
                        background: #343a40;
                        color: white;
                        text-align: center;
                        padding: 25px 20px;
                        font-size: 14px;
                    }
                    .contact-info {
                        margin-top: 15px;
                        font-size: 13px;
                        color: #adb5bd;
                    }
                    .signature {
                        margin-top: 30px;
                        border-top: 2px solid #e9ecef;
                        padding-top: 20px;
                        font-style: italic;
                        color: #6c757d;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🎯 Entreprise XYZ</h1>
                        <p>Service des Ressources Humaines</p>
                    </div>
                    
                    <div class="content">
                        <div class="greeting">Cher(e) %s,</div>
                        
                        <div class="message">
                            Nous accusons réception de votre candidature et vous remercions de l'intérêt 
                            que vous portez à notre entreprise. Votre dossier a été traité de manière 
                            professionnelle et est actuellement en cours d'étude par notre équipe RH.
                        </div>
                        
                        <div class="important-info">
                            <div class="info-title">📋 PROCHAIN ÉTAPE : TEST QCM</div>
                            <div class="info-details">
                                Votre test QCM est programmé pour :<br>
                                <span class="date-time">📅 Demain à 12h00</span>
                            </div>
                        </div>
                        
                        <div class="instructions">
                            <div class="instructions-title">ℹ️ Instructions importantes :</div>
                            <ul class="instructions-list">
                                <li>Le test durera environ 45 minutes</li>
                                <li>Assurez-vous d'être dans un environnement calme</li>
                                <li>Ayez une connexion internet stable</li>
                                <li>Préparez votre pièce d'identité</li>
                                <li>Le lien de connexion vous sera envoyé 30 minutes avant le test</li>
                            </ul>
                        </div>
                        
                        <div class="message">
                            Nous vous encourageons à vous préparer sérieusement à cette évaluation, 
                            qui constitue une étape importante dans le processus de recrutement.
                        </div>
                        
                        <div class="signature">
                            Cordialement,<br>
                            <strong>L'équipe de recrutement</strong><br>
                            Entreprise XYZ<br>
                            📧 recrutement@entreprisexyz.com<br>
                            📞 +33 1 23 45 67 89
                        </div>
                    </div>
                    
                    <div class="footer">
                        <p>© 2024 Entreprise XYZ. Tous droits réservés.</p>
                        <div class="contact-info">
                            Cet email a été envoyé automatiquement, merci de ne pas y répondre.
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(candidateName);
        
        sendHtmlEmail(to, subject, htmlContent);
    }
}