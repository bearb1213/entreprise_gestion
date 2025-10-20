INSERT INTO competence (libelle) VALUES 
('Java'), 
('Spring Boot'), 
('SQL'), 
('Gestion de projet');


INSERT INTO diplome (libelle) VALUES 
('Licence Informatique'), 
('Master Informatique'),
('Licence Gestion'), 
('Master Management');


INSERT INTO filiere (libelle) VALUES 
('Informatique'), 
('Management'), 
('Finance'), 
('Ressources Humaines');


-- Informatique
INSERT INTO diplome_filiere (filiere_id, diplome_id) VALUES (1, 1), (1, 2);

-- Management
INSERT INTO diplome_filiere (filiere_id, diplome_id) VALUES (2, 3), (2, 4);

-- Finance
INSERT INTO diplome_filiere (filiere_id, diplome_id) VALUES (3, 3), (3, 4);

-- Ressources Humaines
INSERT INTO diplome_filiere (filiere_id, diplome_id) VALUES (4, 3), (4, 4);

-- Insertion des langues
INSERT INTO langue (libelle) VALUES 
('Français'),
('Anglais'),
('Malgache'),
('Espagnol'),
('Allemand'),
('Italien'),
('Arabe'),
('Chinois'),
('Russe'),
('Portugais');

INSERT INTO metier (id, libelle) VALUES
(1, 'Développeur Java'),
(2, 'Développeur Python'),
(3, 'Développeur Frontend'),
(4, 'Administrateur Systèmes'),
(5, 'Administrateur Réseau'),
(6, 'Data Analyst'),
(7, 'Data Scientist'),
(8, 'Chef de projet'),
(9, 'Consultant ERP'),
(10, 'Designer UI/UX');


-- ✅ Nouvelles compétences (techniques et soft skills)
INSERT INTO competence (libelle) VALUES  
('Python'),  
('Communication'),  
('Travail en équipe'),  
('Leadership'),  
('Gestion du temps'),  
('Créativité'),  
('Résolution de problèmes'),  
('Adaptabilité'),  
('Esprit critique'),  
('Négociation'),  
('Prise de décision');

-- ✅ Nouveaux métiers hors IT
INSERT INTO metier (id, libelle) VALUES
(11, 'Comptable'),
(12, 'Auditeur financier'),
(13, 'Chargé de communication'),
(14, 'Responsable Marketing'),
(15, 'Consultant en Ressources Humaines'),
(16, 'Psychologue du travail'),
(17, 'Juriste d entreprise'),
(18, 'Médecin du travail'),
(19, 'Responsable logistique'),
(20, 'Chef de produit');



INSERT INTO evaluation (coeff , libelle) VALUES (3 , 'CV');
INSERT INTO evaluation (coeff , libelle) VALUES (2 , 'QCM');
INSERT INTO evaluation (coeff , libelle) VALUES (5 , 'Entretien');


-- ✅ Départements
INSERT INTO departement (libelle) VALUES
('Admin'),
('Rh'),
('Unite'),
('Informatique'),
('Finance'),
('Ressources Humaines'),
('Marketing'),
('Communication'),
('Logistique'),
('Juridique'),
('Santé et Sécurité au travail');

-- ✅ Utilisateurs liés aux départements (corrigés selon tes IDs actuels)
INSERT INTO utilisateur (login, mdp, departement_id) VALUES
('admin', 'admin', 1),                  -- Admin
('rh_admin', 'rh_admin', 2),            -- RH
('unite_admin', 'unite_admin', 3),      -- Unite
('it_admin', 'it_admin', 4),            -- Informatique
('fin_admin', 'fin_admin', 5),          -- Finance
('rh2_admin', 'rh2_admin', 6),          -- Ressources Humaines
('mark_admin', 'mark_admin', 7),        -- Marketing
('com_admin', 'com_admin', 8),          -- Communication
('log_admin', 'log_admin', 9),          -- Logistique
('jur_admin', 'jur_admin', 10),         -- Juridique
('sante_admin', 'sante_admin', 11);     -- Santé & sécurité



-- QUESTIONS BASIQUES (id_departement et id_metier = NULL)
INSERT INTO question (intitule, metier_id) VALUES 
('Comment gérez-vous les délais serrés ?', NULL),
('Décrivez une situation où vous avez dû résoudre un problème complexe.', NULL),
('Comment vous adaptez-vous aux changements organisationnels ?', NULL),
('Quelle est votre approche pour travailler en équipe ?', NULL),
('Comment priorisez-vous votre travail lorsque vous avez plusieurs tâches importantes ?', NULL);

-- CHOIX pour les questions basiques
-- Question 1: Délais serrés
INSERT INTO choix (reponse, coeff, question_id) VALUES 
('Je liste toutes les tâches et les priorise par urgence et importance', 3, 1),
('Je travaille plus d''heures pour tout terminer à temps', 1, 1),
('Je demande une extension de délai immédiatement', 0, 1),
('Je délègue certaines tâches si possible', 2, 1);

-- Question 2: Résolution de problème complexe
INSERT INTO choix (reponse, coeff, question_id) VALUES 
('J''analyse le problème, identifie les causes racines et propose des solutions', 3, 2),
('Je demande immédiatement de l''aide à mon supérieur', 1, 2),
('J''ignore le problème en espérant qu''il se résolve seul', 0, 2),
('Je cherche des solutions similaires déjà utilisées dans l''entreprise', 2, 2);

-- Question 3: Adaptation aux changements
INSERT INTO choix (reponse, coeff, question_id) VALUES 
('Je m''adapte rapidement et vois le changement comme une opportunité', 3, 3),
('Je résiste d''abord puis finis par m''adapter', 1, 3),
('Je refuse le changement si cela perturbe mes habitudes', 0, 3),
('J''évalue les impacts avant de m''adapter', 2, 3);

-- Question 4: Travail en équipe
INSERT INTO choix (reponse, coeff, question_id) VALUES 
('Je communique régulièrement et collabore activement', 3, 4),
('Je préfère travailler seul mais coopère si nécessaire', 1, 4),
('J''évite le travail d''équipe autant que possible', 0, 4),
('Je prends le leadership naturellement', 2, 4);

-- Question 5: Priorisation des tâches
INSERT INTO choix (reponse, coeff, question_id) VALUES 
('J''utilise la matrice Eisenhower (urgent/important)', 3, 5),
('Je traite les tâches dans l''ordre où elles arrivent', 1, 5),
('Je fais d''abord ce qui est le plus facile', 0, 5),
('Je consulte mon manager pour les priorités', 2, 5);

-- QUESTIONS POUR MÉTIERS SPÉCIFIQUES
-- Développeur Java (metier_id: 1)
INSERT INTO question (intitule, metier_id) VALUES 
('Quelle est la différence entre ArrayList et LinkedList en Java ?', 1),
('Expliquez le principe de l''héritage en POO.', 1),
('Comment gérez-vous les exceptions dans Spring Boot ?', 1);

-- CHOIX pour Développeur Java
-- Question 6: ArrayList vs LinkedList
INSERT INTO choix (reponse, coeff, question_id) VALUES 
('ArrayList pour accès aléatoire, LinkedList pour insertions fréquentes', 3, 6),
('LinkedList est toujours plus performante', 0, 6),
('ArrayList utilise moins de mémoire', 1, 6),
('Les deux sont identiques en performance', 0, 6);

-- Question 7: Héritage POO
INSERT INTO choix (reponse, coeff, question_id) VALUES 
('L''héritage permet à une classe d''hériter des propriétés d''une autre', 3, 7),
('L''héritage est utilisé pour le multithreading', 0, 7),
('L''héritage permet de cacher l''implémentation', 1, 7),
('L''héritage est obsolète en Java moderne', 0, 7);

-- Question 8: Exceptions Spring Boot
INSERT INTO choix (reponse, coeff, question_id) VALUES 
('J''utilise @ControllerAdvice pour la gestion globale des exceptions', 3, 8),
('Je catch toutes les exceptions dans chaque méthode', 1, 8),
('Je laisse les exceptions se propager sans gestion', 0, 8),
('J''utilise seulement les exceptions unchecked', 1, 8);

-- Développeur Python (metier_id: 2)
INSERT INTO question (intitule, metier_id) VALUES 
('Quelle est la différence entre liste et tuple en Python ?', 2),
('A quoi servent les décorateurs en Python ?', 2),
('Comment gérez-vous les environnements virtuels ?', 2);

-- CHOIX pour Développeur Python
-- Question 9: Liste vs Tuple
INSERT INTO choix (reponse, coeff, question_id) VALUES 
('Liste mutable, tuple immutable', 3, 9),
('Tuple mutable, liste immutable', 0, 9),
('Liste pour données hétérogènes, tuple pour homogènes', 1, 9),
('Aucune différence significative', 0, 9);

-- Question 10: Décorateurs
INSERT INTO choix (reponse, coeff, question_id) VALUES 
('Ils modifient le comportement des fonctions sans changer leur code', 3, 10),
('Ils servent à commenter le code', 0, 10),
('Ils optimisent la performance', 1, 10),
('Ils gèrent les exceptions', 0, 10);

-- Question 11: Environnements virtuels
INSERT INTO choix (reponse, coeff, question_id) VALUES 
('J''utilise venv ou conda pour isoler les dépendances', 3, 11),
('J''installe tout globalement', 0, 11),
('Je n''utilise pas d''environnements virtuels', 0, 11),
('Je utilise seulement pip sans isolation', 1, 11);

-- Chef de projet (metier_id: 8)
INSERT INTO question (intitule, metier_id) VALUES 
('Comment priorisez-vous les tâches d''un projet ?', 8),
('Quelle méthodologie agile préférez-vous ?', 8),
('Comment gérez-vous les retards dans un projet ?', 8);

-- CHOIX pour Chef de projet
-- Question 12: Priorisation des tâches
INSERT INTO choix (reponse, coeff, question_id) VALUES 
('Par valeur métier et dépendances techniques', 3, 12),
('Par ordre d''arrivée des demandes', 1, 12),
('Selon la complexité technique uniquement', 1, 12),
('Je laisse l''équipe décider', 0, 12);

-- Question 13: Méthodologie agile
INSERT INTO choix (reponse, coeff, question_id) VALUES 
('Je choisis selon le contexte : Scrum pour projets stables, Kanban pour flux continu', 3, 13),
('Scrum uniquement', 2, 13),
('Kanban uniquement', 2, 13),
('Les méthodologies agiles ne sont pas importantes', 0, 13);

-- Question 14: Gestion des retards
INSERT INTO choix (reponse, coeff, question_id) VALUES 
('J''analyse les causes et ajuste le plan avec l''équipe', 3, 14),
('Je demande à l''équipe de travailler plus', 0, 14),
('Je cache les retards aux stakeholders', 0, 14),
('Je supprime des fonctionnalités non essentielles', 2, 14);

-- Data Analyst (metier_id: 6)
INSERT INTO question (intitule, metier_id) VALUES 
('Quels outils utilisez-vous pour la visualisation de données ?', 6),
('Comment nettoyez-vous des données sales ?', 6),
('Comment présentez-vous des résultats à des non-techniques ?', 6);

-- CHOIX pour Data Analyst
-- Question 15: Outils de visualisation
INSERT INTO choix (reponse, coeff, question_id) VALUES 
('Tableau, Power BI, ou librairies Python comme Matplotlib/Seaborn', 3, 15),
('Excel uniquement', 1, 15),
('Je ne fais pas de visualisation', 0, 15),
('J''utilise seulement SQL', 0, 15);

-- Question 16: Nettoyage de données
INSERT INTO choix (reponse, coeff, question_id) VALUES 
('J''identifie et traite les valeurs manquantes, doublons et incohérences', 3, 16),
('Je supprime les lignes avec des problèmes', 1, 16),
('J''ignore les problèmes de qualité', 0, 16),
('Je demande à quelqu''un d''autre de nettoyer', 0, 16);

-- Question 17: Présentation résultats
INSERT INTO choix (reponse, coeff, question_id) VALUES 
('J''utilise des visualisations simples et un langage business', 3, 17),
('Je présente tous les détails techniques', 0, 17),
('Je n''explique pas mes analyses', 0, 17),
('Je délègue la présentation', 1, 17);

-- Consultant RH (metier_id: 15)
INSERT INTO question (intitule, metier_id) VALUES 
('Comment évaluez-vous la culture d''une entreprise ?', 15),
('Quelles méthodes utilisez-vous pour le recrutement ?', 15),
('Comment gérez-vous un conflit entre collaborateurs ?', 15);

-- CHOIX pour Consultant RH
-- Question 18: Évaluation culture d'entreprise
INSERT INTO choix (reponse, coeff, question_id) VALUES 
('J''analyse les valeurs, processus décisionnels et feedback employés', 3, 18),
('Je me base uniquement sur le site web', 0, 18),
('Je n''évalue pas la culture', 0, 18),
('Je demande aux managers uniquement', 1, 18);

-- Question 19: Méthodes de recrutement
INSERT INTO choix (reponse, coeff, question_id) VALUES 
('Entretiens structurés, tests techniques et références', 3, 19),
('Entretien informel uniquement', 1, 19),
('Je recrute sur CV uniquement', 0, 19),
('Je délègue tout au service recrutement', 0, 19);

-- Question 20: Gestion de conflits
INSERT INTO choix (reponse, coeff, question_id) VALUES 
('J''écoute les deux parties et facilite une résolution collaborative', 3, 20),
('Je prends parti pour l''un des collaborateurs', 0, 20),
('J''ignore le conflit', 0, 20),
('Je transfère le problème à la direction', 1, 20);