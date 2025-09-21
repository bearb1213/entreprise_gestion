package com.entreprise.gestion.rh.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.entreprise.gestion.config.CompanyInfos;
import com.entreprise.gestion.exception.MyException;
import com.entreprise.gestion.rh.model.Candidat;
import com.entreprise.gestion.rh.model.Candidature;
import com.entreprise.gestion.rh.model.Personne;
import com.entreprise.gestion.rh.repository.CandidatureRepository;

@Service
public class PdfService {
    @Autowired
    TemplateEngine templateEngine;

    @Autowired
    private CandidatureRepository candidatureRepository;

    public byte[] generatePdf(String htmlContent) {
        try {

            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }    

    
}
