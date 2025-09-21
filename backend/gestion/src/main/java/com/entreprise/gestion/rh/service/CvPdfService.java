package com.entreprise.gestion.rh.service;

import com.entreprise.gestion.rh.model.*;
import com.entreprise.gestion.rh.repository.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import com.itextpdf.text.pdf.draw.LineSeparator;

@Service
@RequiredArgsConstructor
public class CvPdfService {

    private final CandidatRepository candidatRepository;
    private final PersonneRepository personneRepository;
    private final CompetenceRepository competenceRepository;
    private final LangueRepository langueRepository;
    private final DiplomeFiliereRepository diplomeFiliereRepository;
    private final ExperienceRepository experienceRepository;
    private final MetierRepository metierRepository;

    // Palette de couleurs professionnelles
    private static final BaseColor PRIMARY_COLOR = new BaseColor(37, 99, 128);      // Bleu marine professionnel
    private static final BaseColor SECONDARY_COLOR = new BaseColor(88, 129, 87);   // Vert sophistiqué
    private static final BaseColor ACCENT_COLOR = new BaseColor(207, 131, 81);     // Orange doux
    private static final BaseColor TEXT_DARK = new BaseColor(33, 37, 41);         // Gris très foncé
    private static final BaseColor TEXT_LIGHT = new BaseColor(108, 117, 125);     // Gris moyen
    private static final BaseColor BACKGROUND_LIGHT = new BaseColor(248, 249, 250); // Gris très clair
    private static final BaseColor WHITE = BaseColor.WHITE;

    /**
     * Vérifie si un CV peut être généré pour le candidat
     */
    public boolean checkCvExists(Integer candidatId) {
        return candidatRepository.existsById(candidatId);
    }

    /**
     * Génère le PDF du CV avec un design professionnel
     */
    public ByteArrayInputStream generateCvPdf(Integer candidatId) throws DocumentException {
        // Récupérer les données du candidat
        Candidat candidat = candidatRepository.findById(candidatId)
                .orElseThrow(() -> new RuntimeException("Candidat non trouvé"));
        
        Personne personne = personneRepository.findById(candidat.getPersonne().getId())
                .orElseThrow(() -> new RuntimeException("Personne non trouvée"));
        
        List<Competence> competences = competenceRepository.findByCandidatId(candidatId);
        List<Langue> langues = langueRepository.findByCandidatId(candidatId);
        List<DiplomeFiliere> diplomesFilieres = diplomeFiliereRepository.findByCandidatId(candidatId);
        List<Experience> experiences = experienceRepository.findByCandidatId(candidatId);

        // Configuration du document PDF
        Document document = new Document(PageSize.A4, 0, 0, 0, 0);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, out);
        
        // Pas d'événement de page personnalisé
        document.open();

        // Définition des polices professionnelles
        com.itextpdf.text.Font nameFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 28, com.itextpdf.text.Font.BOLD, WHITE);
        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.NORMAL, WHITE);
        com.itextpdf.text.Font sectionFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD, PRIMARY_COLOR);
        com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.NORMAL, TEXT_DARK);
        com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD, TEXT_DARK);
        com.itextpdf.text.Font lightFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.NORMAL, TEXT_LIGHT);

        // Table principale avec deux colonnes (sidebar + contenu principal)
        PdfPTable mainTable = new PdfPTable(new float[]{35f, 65f});
        mainTable.setWidthPercentage(100);
        mainTable.setSpacingAfter(0);

        // SIDEBAR GAUCHE (informations personnelles)
        PdfPCell sidebarCell = createSidebarContent(personne, candidat, competences, langues, 
                                                   nameFont, titleFont, normalFont, boldFont, lightFont);
        mainTable.addCell(sidebarCell);

        // CONTENU PRINCIPAL DROITE
        PdfPCell mainContentCell = createMainContent(experiences, diplomesFilieres, candidat,
                                                   sectionFont, normalFont, boldFont, lightFont);
        mainTable.addCell(mainContentCell);

        document.add(mainTable);
        // Ajouter un pied de page simple
        Paragraph footer = new Paragraph("Page 1", 
                new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 9, com.itextpdf.text.Font.NORMAL, TEXT_LIGHT));
        footer.setAlignment(Element.ALIGN_RIGHT);
        footer.setSpacingBefore(20);
        document.add(footer);

        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    private PdfPCell createSidebarContent(Personne personne, Candidat candidat, 
                                        List<Competence> competences, List<Langue> langues,
                                        com.itextpdf.text.Font nameFont, com.itextpdf.text.Font titleFont,
                                        com.itextpdf.text.Font normalFont, com.itextpdf.text.Font boldFont,
                                        com.itextpdf.text.Font lightFont) throws DocumentException {
        
        PdfPCell sidebarCell = new PdfPCell();
        sidebarCell.setBackgroundColor(PRIMARY_COLOR);
        sidebarCell.setBorder(PdfPCell.NO_BORDER);
        sidebarCell.setPaddingLeft(25);
        sidebarCell.setPaddingRight(25);
        sidebarCell.setPaddingTop(40);
        sidebarCell.setPaddingBottom(40);

        // Photo de profil
        if (personne.getImage() != null && !personne.getImage().isEmpty()) {
            try {
                Image photo = Image.getInstance(personne.getImage());
                photo.scaleToFit(120, 120);
                photo.setAlignment(Element.ALIGN_CENTER);
                
                // Création d'un conteneur centré pour la photo
                PdfPTable photoTable = new PdfPTable(1);
                photoTable.setWidthPercentage(100);
                PdfPCell photoCell = new PdfPCell();
                photoCell.setBorder(PdfPCell.NO_BORDER);
                photoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                photoCell.setPaddingBottom(20);
                photoCell.addElement(photo);
                photoTable.addCell(photoCell);
                
                sidebarCell.addElement(photoTable);
            } catch (Exception e) {
                // Placeholder élégant
                Paragraph noPhoto = new Paragraph("📷", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 40, com.itextpdf.text.Font.NORMAL, WHITE));
                noPhoto.setAlignment(Element.ALIGN_CENTER);
                noPhoto.setSpacingAfter(20);
                sidebarCell.addElement(noPhoto);
            }
        }

        // Nom et prénom
        Paragraph name = new Paragraph(personne.getPrenom() + " " + personne.getNom().toUpperCase(), nameFont);
        name.setAlignment(Element.ALIGN_CENTER);
        name.setSpacingAfter(5);
        sidebarCell.addElement(name);

        // Titre professionnel (basé sur l'expérience principale)
        String titreProf = "Candidat";
        if (!candidat.getExperiences().isEmpty()) {
            titreProf = candidat.getExperiences().get(0).getMetier().getLibelle();
        }
        Paragraph titre = new Paragraph(titreProf, titleFont);
        titre.setAlignment(Element.ALIGN_CENTER);
        titre.setSpacingAfter(25);
        sidebarCell.addElement(titre);

        // Contact
        addSidebarSection("CONTACT", sidebarCell, boldFont, WHITE);
        addContactItem("✉", personne.getEmail(), sidebarCell, normalFont, WHITE);
        addContactItem("📞", personne.getTelephone(), sidebarCell, normalFont, WHITE);
        addContactItem("📍", personne.getVille(), sidebarCell, normalFont, WHITE);
        
        if (personne.getDateNaissance() != null) {
            int age = Period.between(personne.getDateNaissance(), LocalDate.now()).getYears();
            addContactItem("🎂", age + " ans", sidebarCell, normalFont, WHITE);
        }

        // Compétences
        if (competences != null && !competences.isEmpty()) {
            addSidebarSection("COMPÉTENCES", sidebarCell, boldFont, WHITE);
            for (Competence competence : competences) {
                addSkillItem(competence.getLibelle(), sidebarCell, normalFont, lightFont);
            }
        }

        // Langues
        if (langues != null && !langues.isEmpty()) {
            addSidebarSection("LANGUES", sidebarCell, boldFont, WHITE);
            for (Langue langue : langues) {
                addLanguageItem(langue.getLibelle(), "Maîtrisé", sidebarCell, normalFont, lightFont);
            }
        }

        return sidebarCell;
    }

    private PdfPCell createMainContent(List<Experience> experiences, List<DiplomeFiliere> diplomesFilieres,
                                     Candidat candidat, com.itextpdf.text.Font sectionFont,
                                     com.itextpdf.text.Font normalFont, com.itextpdf.text.Font boldFont,
                                     com.itextpdf.text.Font lightFont) throws DocumentException {
        
        PdfPCell mainContentCell = new PdfPCell();
        mainContentCell.setBackgroundColor(WHITE);
        mainContentCell.setBorder(PdfPCell.NO_BORDER);
        mainContentCell.setPaddingLeft(30);
        mainContentCell.setPaddingRight(30);
        mainContentCell.setPaddingTop(40);
        mainContentCell.setPaddingBottom(40);

        // Profil professionnel
        if (candidat.getDescription() != null && !candidat.getDescription().isEmpty()) {
            addMainSection("PROFIL PROFESSIONNEL", mainContentCell, sectionFont);
            Paragraph description = new Paragraph(candidat.getDescription(), normalFont);
            description.setAlignment(Element.ALIGN_JUSTIFIED);
            description.setSpacingAfter(25);
            description.setLeading(0, 1.4f);
            mainContentCell.addElement(description);
        }

        // Expérience professionnelle
        if (experiences != null && !experiences.isEmpty()) {
            addMainSection("EXPÉRIENCE PROFESSIONNELLE", mainContentCell, sectionFont);
            
            for (Experience experience : experiences) {
                Metier metier = metierRepository.findById(experience.getMetier().getId().intValue())
                        .orElse(new Metier());
                
                // Titre du poste avec design moderne
                PdfPTable expTable = new PdfPTable(2);
                expTable.setWidthPercentage(100);
                expTable.setSpacingBefore(5);
                expTable.setSpacingAfter(15);
                
                PdfPCell titleCell = new PdfPCell(new Phrase(metier.getLibelle(), boldFont));
                titleCell.setBorder(PdfPCell.NO_BORDER);
                titleCell.setPaddingBottom(5);
                
                PdfPCell durationCell = new PdfPCell(new Phrase(experience.getNbAnnee() + " année(s)", lightFont));
                durationCell.setBorder(PdfPCell.NO_BORDER);
                durationCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                durationCell.setPaddingBottom(5);
                
                expTable.addCell(titleCell);
                expTable.addCell(durationCell);
                
                mainContentCell.addElement(expTable);
                
                // Ligne de séparation subtile
                addSubtleSeparator(mainContentCell);
            }
        }

        // Formation
        if (diplomesFilieres != null && !diplomesFilieres.isEmpty()) {
            addMainSection("FORMATION", mainContentCell, sectionFont);
            
            for (DiplomeFiliere diplomeFiliere : diplomesFilieres) {
                PdfPTable formationTable = new PdfPTable(1);
                formationTable.setWidthPercentage(100);
                formationTable.setSpacingBefore(5);
                formationTable.setSpacingAfter(10);
                
                String formation = diplomeFiliere.getDiplome().getLibelle();
                String filiere = diplomeFiliere.getFiliere().getLibelle();
                
                Paragraph formationTitle = new Paragraph(formation, boldFont);
                Paragraph filiereDesc = new Paragraph(filiere, normalFont);
                filiereDesc.setSpacingAfter(5);
                
                PdfPCell formationCell = new PdfPCell();
                formationCell.setBorder(PdfPCell.NO_BORDER);
                formationCell.addElement(formationTitle);
                formationCell.addElement(filiereDesc);
                formationCell.setPaddingLeft(10);
                formationCell.setBorderWidthLeft(3);
                formationCell.setBorderColorLeft(ACCENT_COLOR);
                
                formationTable.addCell(formationCell);
                mainContentCell.addElement(formationTable);
            }
        }

        return mainContentCell;
    }

    private void addSidebarSection(String title, PdfPCell cell, com.itextpdf.text.Font font, BaseColor color) throws DocumentException {
        Paragraph sectionTitle = new Paragraph(title, new com.itextpdf.text.Font(font.getBaseFont(), 12, com.itextpdf.text.Font.BOLD, color));
        sectionTitle.setSpacingBefore(20);
        sectionTitle.setSpacingAfter(10);
        cell.addElement(sectionTitle);
        
        // Ligne de séparation
        LineSeparator separator = new LineSeparator();
        separator.setLineColor(ACCENT_COLOR);
        separator.setLineWidth(2);
        Paragraph separatorPara = new Paragraph();
        separatorPara.add(new Chunk(separator));
        separatorPara.setSpacingAfter(15);
        cell.addElement(separatorPara);
    }

    private void addMainSection(String title, PdfPCell cell, com.itextpdf.text.Font font) throws DocumentException {
        Paragraph sectionTitle = new Paragraph(title, font);
        sectionTitle.setSpacingBefore(15);
        sectionTitle.setSpacingAfter(15);
        cell.addElement(sectionTitle);
        
        // Ligne de séparation moderne
        LineSeparator separator = new LineSeparator();
        separator.setLineColor(SECONDARY_COLOR);
        separator.setLineWidth(3);
        Paragraph separatorPara = new Paragraph();
        separatorPara.add(new Chunk(separator));
        separatorPara.setSpacingAfter(20);
        cell.addElement(separatorPara);
    }

    private void addContactItem(String icon, String text, PdfPCell cell, com.itextpdf.text.Font font, BaseColor color) {
        if (text != null && !text.trim().isEmpty()) {
            Paragraph contact = new Paragraph();
            contact.add(new Chunk(icon + " ", new com.itextpdf.text.Font(font.getBaseFont(), 10, com.itextpdf.text.Font.NORMAL, ACCENT_COLOR)));
            contact.add(new Chunk(text, new com.itextpdf.text.Font(font.getBaseFont(), 10, com.itextpdf.text.Font.NORMAL, color)));
            contact.setSpacingAfter(8);
            cell.addElement(contact);
        }
    }

    private void addSkillItem(String skill, PdfPCell cell, com.itextpdf.text.Font font, com.itextpdf.text.Font lightFont) {
        Paragraph skillPara = new Paragraph();
        skillPara.add(new Chunk("● ", new com.itextpdf.text.Font(font.getBaseFont(), 10, com.itextpdf.text.Font.NORMAL, ACCENT_COLOR)));
        skillPara.add(new Chunk(skill, new com.itextpdf.text.Font(font.getBaseFont(), 10, com.itextpdf.text.Font.NORMAL, WHITE)));
        skillPara.setSpacingAfter(6);
        cell.addElement(skillPara);
    }

    private void addLanguageItem(String langue, String niveau, PdfPCell cell, com.itextpdf.text.Font font, com.itextpdf.text.Font lightFont) {
        PdfPTable langTable = new PdfPTable(2);
        langTable.setWidthPercentage(100);
        langTable.setSpacingAfter(6);
        
        PdfPCell langCell = new PdfPCell(new Phrase(langue, new com.itextpdf.text.Font(font.getBaseFont(), 10, com.itextpdf.text.Font.NORMAL, WHITE)));
        langCell.setBorder(PdfPCell.NO_BORDER);
        
        PdfPCell niveauCell = new PdfPCell(new Phrase(niveau, new com.itextpdf.text.Font(lightFont.getBaseFont(), 9, com.itextpdf.text.Font.ITALIC, BACKGROUND_LIGHT)));
        niveauCell.setBorder(PdfPCell.NO_BORDER);
        niveauCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        
        langTable.addCell(langCell);
        langTable.addCell(niveauCell);
        cell.addElement(langTable);
    }

    private void addSubtleSeparator(PdfPCell cell) throws DocumentException {
        LineSeparator separator = new LineSeparator();
        separator.setLineColor(BACKGROUND_LIGHT);
        separator.setLineWidth(1);
        Paragraph separatorPara = new Paragraph();
        separatorPara.add(new Chunk(separator));
        separatorPara.setSpacingAfter(10);
        cell.addElement(separatorPara);
    }


}