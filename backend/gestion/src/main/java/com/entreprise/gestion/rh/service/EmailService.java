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
}