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

}