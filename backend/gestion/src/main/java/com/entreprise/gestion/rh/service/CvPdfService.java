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

    // Couleurs pour le design
    private static final BaseColor HEADER_COLOR = new BaseColor(41, 128, 185);
    private static final BaseColor SECTION_COLOR = new BaseColor(52, 152, 219);
    private static final BaseColor TEXT_COLOR = new BaseColor(44, 62, 80);

    /**
     * Vérifie si un CV peut être généré pour le candidat
     */
    public boolean checkCvExists(Integer candidatId) {
        return candidatRepository.existsById(candidatId);
    }

    /**
     * Génère le PDF du CV
     */
    public ByteArrayInputStream generateCvPdf(Integer candidatId) throws DocumentException {
        // Récupérer les données du candidat - conversion de Integer en Integer si nécessaire
        Candidat candidat = candidatRepository.findById(candidatId)
                .orElseThrow(() -> new RuntimeException("Candidat non trouvé"));
        
        Personne personne = personneRepository.findById(candidat.getPersonne().getId())
                .orElseThrow(() -> new RuntimeException("Personne non trouvée"));
        
        List<Competence> competences = competenceRepository.findByCandidatId(candidatId);
        List<Langue> langues = langueRepository.findByCandidatId(candidatId);
        List<DiplomeFiliere> diplomesFilieres = diplomeFiliereRepository.findByCandidatId(candidatId);
        List<Experience> experiences = experienceRepository.findByCandidatId(candidatId);

        // Configuration du document PDF
        Document document = new Document(PageSize.A4, 36, 36, 90, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, out);
        
        // Ajouter un en-tête et pied de page personnalisé
        writer.setPageEvent(new HeaderFooterPageEvent());
        
        document.open();

        // Police personnalisée
        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 22, com.itextpdf.text.Font.BOLD, BaseColor.WHITE);
        com.itextpdf.text.Font sectionFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.BOLD, SECTION_COLOR);
        com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.NORMAL, TEXT_COLOR);
        com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 11, com.itextpdf.text.Font.BOLD, TEXT_COLOR);

        // En-tête avec informations personnelles
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setSpacingBefore(20);
        headerTable.setSpacingAfter(20);
        
        // Cellule pour les informations personnelles
        PdfPCell infoCell = new PdfPCell();
        infoCell.setBorder(PdfPCell.NO_BORDER);
        infoCell.setPadding(10);
        
        // Nom et prénom
        Paragraph name = new Paragraph(personne.getPrenom() + " " + personne.getNom().toUpperCase(), titleFont);
        name.setAlignment(Element.ALIGN_LEFT);
        infoCell.addElement(name);
        
        // Informations de contact
        addContactInfo(infoCell, "Email: " + personne.getEmail(), normalFont);
        addContactInfo(infoCell, "Téléphone: " + personne.getTelephone(), normalFont);
        addContactInfo(infoCell, "Ville: " + personne.getVille(), normalFont);
        
        if (personne.getDateNaissance() != null) {
            int age = Period.between(personne.getDateNaissance(), LocalDate.now()).getYears();
            addContactInfo(infoCell, "Âge: " + age + " ans", normalFont);
        }
        
        // Cellule pour la photo (si disponible)
        PdfPCell photoCell = new PdfPCell();
        photoCell.setBorder(PdfPCell.NO_BORDER);
        photoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        
        if (personne.getImage() != null && !personne.getImage().isEmpty()) {
            try {
                Image photo = Image.getInstance(personne.getImage());
                photo.scaleToFit(80, 80);
                photoCell.addElement(photo);
            } catch (Exception e) {
                // En cas d'erreur de chargement de l'image, on affiche un placeholder
                Paragraph noPhoto = new Paragraph("Photo non disponible", normalFont);
                photoCell.addElement(noPhoto);
            }
        }
        
        headerTable.addCell(infoCell);
        headerTable.addCell(photoCell);
        
        // Appliquer un fond coloré à l'en-tête
        for (PdfPCell cell : headerTable.getRows().get(0).getCells()) {
            cell.setBackgroundColor(HEADER_COLOR);
        }
        
        document.add(headerTable);
        
        // Description du candidat
        if (candidat.getDescription() != null && !candidat.getDescription().isEmpty()) {
            addSectionTitle("Profil", sectionFont, document);
            Paragraph description = new Paragraph(candidat.getDescription(), normalFont);
            description.setSpacingAfter(15);
            document.add(description);
        }
        
        // Compétences
        if (competences != null && !competences.isEmpty()) {
            addSectionTitle("Compétences", sectionFont, document);
            
            PdfPTable competencesTable = new PdfPTable(2);
            competencesTable.setWidthPercentage(100);
            competencesTable.setSpacingBefore(5);
            competencesTable.setSpacingAfter(15);
            
            for (Competence competence : competences) {
                PdfPCell cell = new PdfPCell(new Phrase("• " + competence.getLibelle(), normalFont));
                cell.setBorder(PdfPCell.NO_BORDER);
                competencesTable.addCell(cell);
            }
            
            // Remplir les cellules vides pour un affichage correct
            if (competences.size() % 2 != 0) {
                PdfPCell emptyCell = new PdfPCell(new Phrase(""));
                emptyCell.setBorder(PdfPCell.NO_BORDER);
                competencesTable.addCell(emptyCell);
            }
            
            document.add(competencesTable);
        }
        
        // Expériences professionnelles
        if (experiences != null && !experiences.isEmpty()) {
            addSectionTitle("Expériences professionnelles", sectionFont, document);
            
            for (Experience experience : experiences) {
                Metier metier = metierRepository.findById(experience.getMetier().getId().intValue())
                        .orElse(new Metier());
                
                Paragraph expTitle = new Paragraph(metier.getLibelle(), boldFont);
                Paragraph expDuration = new Paragraph(experience.getNbAnnee() + " année(s) d'expérience", normalFont);
                expDuration.setSpacingAfter(5);
                
                document.add(expTitle);
                document.add(expDuration);
            }
            document.add(new Paragraph(" ")); // Espacement
        }
        
        // Diplômes et filières
        if (diplomesFilieres != null && !diplomesFilieres.isEmpty()) {
            addSectionTitle("Formation", sectionFont, document);
            
            for (DiplomeFiliere diplomeFiliere : diplomesFilieres) {
                String formation = diplomeFiliere.getDiplome().getLibelle() + " - " + 
                                  diplomeFiliere.getFiliere().getLibelle();
                
                Paragraph diplome = new Paragraph("• " + formation, normalFont);
                document.add(diplome);
            }
            document.add(new Paragraph(" ")); // Espacement
        }
        
        // Langues
        if (langues != null && !langues.isEmpty()) {
            addSectionTitle("Langues", sectionFont, document);
            
            StringBuilder languesStr = new StringBuilder();
            for (int i = 0; i < langues.size(); i++) {
                languesStr.append(langues.get(i).getLibelle());
                if (i < langues.size() - 1) {
                    languesStr.append(", ");
                }
            }
            
            Paragraph languesParagraph = new Paragraph(languesStr.toString(), normalFont);
            languesParagraph.setSpacingAfter(15);
            document.add(languesParagraph);
        }
        
        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }
    
    private void addContactInfo(PdfPCell cell, String text, com.itextpdf.text.Font font) {
        Paragraph p = new Paragraph(text, font);
        p.setSpacingAfter(2);
        cell.addElement(p);
    }
    
    private void addSectionTitle(String title, com.itextpdf.text.Font font, Document document) throws DocumentException {
        Paragraph sectionTitle = new Paragraph(title, font);
        sectionTitle.setSpacingBefore(10);
        sectionTitle.setSpacingAfter(5);
        document.add(sectionTitle);
        
        // Ligne de séparation - Correction de setColor
        Paragraph separator = new Paragraph();
        separator.setSpacingAfter(10);
        Chunk line = new Chunk("_________________________________________________________________________");
        // Correction: Utiliser setFont avec une police de la bonne couleur
        com.itextpdf.text.Font lineFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10, com.itextpdf.text.Font.NORMAL, SECTION_COLOR);
        line.setFont(lineFont);
        separator.add(line);
        document.add(separator);
    }
    
    // Classe interne pour l'en-tête et pied de page
    class HeaderFooterPageEvent extends PdfPageEventHelper {
        private PdfTemplate template;

        public void onOpenDocument(PdfWriter writer, Document document) {
            template = writer.getDirectContent().createTemplate(30, 16);
        }

        public void onEndPage(PdfWriter writer, Document document) {
            PdfPTable footer = new PdfPTable(3);
            footer.setTotalWidth(527);
            footer.setLockedWidth(true);
            
            // Cellule vide pour centrer le texte
            PdfPCell emptyCell = new PdfPCell(new Phrase(""));
            emptyCell.setBorder(PdfPCell.NO_BORDER);
            footer.addCell(emptyCell);
            
            // Numéro de page
            PdfPCell pageNumber = new PdfPCell(new Phrase("Page " + writer.getPageNumber(), 
                    new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 9, com.itextpdf.text.Font.NORMAL, BaseColor.GRAY)));
            pageNumber.setHorizontalAlignment(Element.ALIGN_CENTER);
            pageNumber.setBorder(PdfPCell.NO_BORDER);
            footer.addCell(pageNumber);
            
            // Cellule vague pour l'alignement
            footer.addCell(emptyCell);
            
            // Positionner le pied de page
            footer.writeSelectedRows(0, -1, 34, 50, writer.getDirectContent());
        }
    }
}