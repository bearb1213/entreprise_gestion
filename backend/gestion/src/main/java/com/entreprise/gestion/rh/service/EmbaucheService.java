package com.entreprise.gestion.rh.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.entreprise.gestion.config.CompanyInfos;
import com.entreprise.gestion.exception.MyException;
import com.entreprise.gestion.rh.model.Candidat;
import com.entreprise.gestion.rh.model.Candidature;
import com.entreprise.gestion.rh.model.Employe;
import com.entreprise.gestion.rh.model.Personne;
import com.entreprise.gestion.rh.repository.CandidatureRepository;
import com.entreprise.gestion.rh.repository.EmployeRepository;

@Service
public class EmbaucheService {
    
    @Autowired
    private CompanyInfos companyInfos;

    @Autowired
    private CandidatureRepository candidatureRepository;

    @Autowired 
    private EmployeRepository employeRepository;

    @Autowired
    private EmailService emailService ;

    @Autowired 
    private PdfService pdfService;

    private void generateMailContrat(Integer idCandidature,LocalDate debutContrat) throws Exception
    {

        Candidature candidature = candidatureRepository.findById(idCandidature).orElseThrow(()-> new MyException("Candidature introuvable,embauche impossible"));
        Candidat candidat = candidature.getCandidat();
        String to = candidat.getPersonne().getEmail();
        String nomCandidat = candidat.getPersonne().getNom()+" "+candidat.getPersonne().getPrenom();
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
                        <h1> Candidature selectionnée pour contrat d'essai </h1>
                    </div>
                    <div>
                        <h2>Bonjour, %s !</h2>
                        <p>Nous sommes heureux de vous annoncer que votre candidature au poste de %s a été retenue pour une phase d'essai.</p>
                        <p>Vous trouverez en PJ une copie de votre contrat</p>
                        <p>Cordialement.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(nomCandidat,candidature.getBesoin().getMetier().getLibelle());
            emailService.sendHtmlEmailWithAttachment(to, "Annonce de contrat d'essai", htmlContent, "Contrat d'essai", generateContratDEssaiPdf(idCandidature, debutContrat), "application/pdf");


        //on va generer ici le contenu du mail puis y joindre le fichier pdf
    }
    @Transactional
    public Employe embaucherEmploye(Integer idCandidature,LocalDate debutContrat) throws Exception
    {
        /*
         * 1-generena ny mail
         * 2-creer-na ilay employe vaovao
         * 3-retourner-na ny Employe sauvegarde sao misy ilavana ny id any avy eo
         * (na tokony misy hierarchie ny ecrans eto dia mivezivezy kely vao mety)
         */
        generateMailContrat(idCandidature,debutContrat);
        Candidature candidature = candidatureRepository.findById(idCandidature)
        .orElseThrow(() -> new MyException("Candidature non trouvée avec l'ID: " + idCandidature));
        Employe employe = new Employe();
        employe.setPersonne(candidature.getCandidat().getPersonne());
        return employeRepository.save(employe);
    }

    public byte[] generateContratDEssaiPdf(Integer idCandidature,LocalDate dateDebut) throws Exception
    {

        /*
         * Infos a avoir:
         *  -infos de l'entreprise (CompanyInfos)
         *  -infos DG (blank aloha) -- quand on y pense,ca pourrait aussi etre l'user qui finalise la procedure,ilay mikitika anle bouton "engager pour un essai"
         *  -infos employe
         *  -debut et fin contrat
         *  -salaire (blank aloha)
         *  -heures de travail(CompanyInfos)
         *  -signature DG(tode atao anarany =>mila ny infos any)
         */

         
        String nomEntreprise = companyInfos.getName();
        String adresseEntreprise = companyInfos.getAddress();
        String villeEntreprise = companyInfos.getCity();
        Integer openingTime = companyInfos.getOpeningTime();
        Integer closingTime = companyInfos.getClosingTime();
        Integer workDaysPerWeek = companyInfos.getWorkDaysPerWeek();
        Integer workHoursPerWeek = (closingTime-openingTime)*workDaysPerWeek;
        String dg = "Monsieur DG";
        String fonctionDg = "Directeur General";
        Candidature candidature = candidatureRepository.findById(idCandidature).orElseThrow(()->new MyException("Candidature inexistante,erreur lors de la creation du contrat d'essai"));
        Personne candidat = candidature.getCandidat().getPersonne();
        String poste = candidature.getBesoin().getMetier().getLibelle();
        Integer dureeContrat = 6; //mila tehirizina somewhere ahhhh!!! Conf ve?
        LocalDate dateActuelle = LocalDate.now();
        String debutContrat = dateDebut.toString();
        String finContrat = dateActuelle.plusMonths(dureeContrat).toString();

        Integer salaire = 2000000;

        // pour amelioration dia afaka ataontsika anaty html ity code lava be ty dia alaina fotsiny avy eo fa masosotra kely fotsiny ny mandefa args any amle izy
        String htmlContent = """

            <!DOCTYPE html>
                <html lang="fr">
                <head>
                    <meta charset="UTF-8"/>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
                    <title>Contrat d'Essai Professionnel</title>
                    <style>
                        /* Styles généraux */
                        * {
                            margin: 0;
                            padding: 0;
                            box-sizing: border-box;
                            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        }
                        
                        body {
                            background-color: #f8f9fa;
                            color: #333;
                            line-height: 1.6;
                            padding: 20px;
                        }
                        
                        .container {
                            max-width: 800px;
                            margin: 0 auto;
                            background: white;
                            padding: 30px;
                            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
                            border-radius: 8px;
                        }
                        
                        /* En-tête */
                        header {
                            text-align: center;
                            margin-bottom: 30px;
                            border-bottom: 2px solid #2c3e50;
                            padding-bottom: 20px;
                        }
                        
                        h1 {
                            color: #2c3e50;
                            font-size: 28px;
                            margin-bottom: 10px;
                        }
                        
                        .subtitle {
                            color: #7f8c8d;
                            font-size: 16px;
                        }
                        
                        /* Sections */
                        section {
                            margin-bottom: 25px;
                        }
                        
                        h2 {
                            color: #2c3e50;
                            font-size: 20px;
                            margin-bottom: 15px;
                            padding-bottom: 5px;
                            border-bottom: 1px solid #ecf0f1;
                        }
                        
                        p {
                            margin-bottom: 15px;
                            text-align: justify;
                        }
                        
                        /* Informations des parties */
                        .parties {
                            display: flex;
                            flex-wrap: wrap;
                            justify-content: space-between;
                            margin-bottom: 20px;
                        }
                        
                        .partie {
                            width: 48%%;
                            margin-bottom: 15px;
                        }
                        
                        .partie h3 {
                            color: #2c3e50;
                            font-size: 18px;
                            margin-bottom: 10px;
                        }
                        
                        .info-item {
                            margin-bottom: 8px;
                        }
                        
                        .info-item strong {
                            display: inline-block;
                            width: 120px;
                            color: #2c3e50;
                        }
                        
                        /* Clauses */
                        .clause {
                            margin-bottom: 20px;
                        }
                        
                        .clause h3 {
                            color: #2c3e50;
                            font-size: 18px;
                            margin-bottom: 10px;
                        }
                        
                        /* Signature */
                        .signatures {
                            display: flex;
                            justify-content: space-between;
                            margin-top: 40px;
                            flex-wrap: wrap;
                        }
                        
                        .signature-block {
                            width: 45%%;
                            margin-top: 30px;
                        }
                        
                        .signature-line {
                            border-top: 1px solid #333;
                            margin-bottom: 10px;
                            padding-top: 5px;
                        }
                        
                        /* Responsive */
                        @media (max-width: 768px) {
                            .partie, .signature-block {
                                width: 100%%;
                            }
                            
                            .parties, .signatures {
                                flex-direction: column;
                            }
                        }
                        
                        /* Bouton d'impression */
                        .print-btn {
                            display: block;
                            margin: 30px auto;
                            padding: 12px 25px;
                            background-color: #2c3e50;
                            color: white;
                            border: none;
                            border-radius: 4px;
                            cursor: pointer;
                            font-size: 16px;
                            transition: background-color 0.3s;
                        }
                        
                        .print-btn:hover {
                            background-color: #1a252f;
                        }
                        
                        footer {
                            text-align: center;
                            margin-top: 30px;
                            color: #7f8c8d;
                            font-size: 14px;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <header>
                            <h1>CONTRAT D'ESSAI PROFESSIONNEL</h1>
                            <p class="subtitle">En accord avec le Code du travail français</p>
                        </header>
                        
                        <section>
                            <h2>ENTRE LES SOUSSIGNÉS</h2>
                            <div class="parties">
                                <div class="partie">
                                    <h3>L'Employeur</h3>
                                    <div class="info-item"><strong>Nom :</strong> %s </div>
                                    <div class="info-item"><strong>Adresse :</strong> %s </div>
                                    <div class="info-item"><strong>Représenté par :</strong> %s </div>
                                    <div class="info-item"><strong>Fonction :</strong> %s </div>
                                </div>
                                
                                <div class="partie">
                                    <h3>Le Salarié</h3>
                                    <div class="info-item"><strong>Nom :</strong> %s </div>
                                    <div class="info-item"><strong>Prénom :</strong> %s </div>
                                    <div class="info-item"><strong>Adresse :</strong> %s </div>
                                    <div class="info-item"><strong>Poste :</strong> %s</div>
                                </div>
                            </div>
                        </section>
                        
                        <section>
                            <h2>OBJET DU CONTRAT</h2>
                            <p>Le présent contrat a pour objet de définir les conditions dans lesquelles <strong> %s </strong> effectuera une période d'essai au sein de %s pour le poste de <strong> %s </strong> .</p>
                        </section>
                        
                        <section>
                            <h2>CLAUSES CONTRACTUELLES</h2>
                            
                            <div class="clause">
                                <h3>Article 1 - Durée de la période d'essai</h3>
                                <p>La période d'essai débutera le <strong> %s </strong> et pourra se prolonger jusqu'au <strong> %s </strong>, conformément aux dispositions du Code du travail.</p>
                            </div>
                            
                            <div class="clause">
                                <h3>Article 2 - Rémunération</h3>
                                <p>Le salarié percevra une rémunération mensuelle brute de [%s] euros, correspondant au poste occupé.</p>
                            </div>
                            
                            <div class="clause">
                                <h3>Article 3 - Horaires de travail</h3>
                                <p>La durée du travail est fixée à %s heures par semaine, soit %s jours de travail, de <strong> %s </strong> à <strong> %s </strong>.</p>
                            </div>
                            
                            <div class="clause">
                                <h3>Article 4 - Confidentialité</h3>
                                <p>Le salarié s'engage à respecter la confidentialité des informations relatives à l'entreprise auxquelles il pourrait avoir accès durant sa période d'essai.</p>
                            </div>
                            
                            <div class="clause">
                                <h3>Article 5 - Rupture de la période d'essai</h3>
                                <p>La période d'essai peut être rompue par l'une ou l'autre des parties à tout moment, dans le respect des dispositions légales et conventionnelles.</p>
                            </div>
                        </section>
                        
                        <section>
                            <h2>LIEU DE TRAVAIL</h2>
                            <p>Le salarié exercera ses fonctions à %s , sous la responsabilité de [Nom du responsable].</p>
                        </section>
                        
                        <div class="signatures">
                            <div class="signature-block">
                                <p>Fait à <strong>%s</strong>, le <strong>%s</strong> </p>
                                <p>Pour l'Employeur</p>
                                <br/>
                                <br/>
                                <div class="signature-line"></div>
                                <p><strong>%s</strong></p>
                            </div>
                            
                            <div class="signature-block">
                                <p>Fait à ....................., le.............</p>
                                <p>Le Salarié</p>
                                <br/>
                                <br/>
                                <div class="signature-line"></div>
                                <p><strong>%s</strong></p>
                            </div>
                        </div>
                    </div>
                </body>
                </html>

                """.formatted(
                nomEntreprise,adresseEntreprise
                ,dg,fonctionDg,
                candidat.getNom(),candidat.getPrenom(),candidat.getVille()
                ,poste
                ,candidat.getNom()+" "+candidat.getPrenom(),nomEntreprise,poste
                ,debutContrat,finContrat
                ,salaire
                ,workHoursPerWeek,workDaysPerWeek,openingTime,closingTime
                ,adresseEntreprise
                ,dg,villeEntreprise,dateActuelle.toString()
                ,candidat.getNom()+" "+candidat.getPrenom()
                );
        return pdfService.generatePdf(htmlContent);
    }
}
