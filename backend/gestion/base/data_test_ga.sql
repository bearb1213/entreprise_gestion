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




-- Besoins
INSERT INTO besoin (statut, min_age, max_age, nb_poste_dsipo, coeff_age, coeff_experience, min_experience, metier_id, departement_id) VALUES
(1, 22, 35, 2, 1, 2, 1, 1, 4),  -- Développeur Java -> IT
(1, 23, 40, 1, 1, 2, 2, 2, 4),  -- Développeur Python -> IT
(1, 25, 45, 1, 1, 2, 3, 6, 3);  -- Data Analyst -> Finance


-- Besoin compétences
-- Besoin 1 (Développeur Java) -> Java, Spring Boot
INSERT INTO besoin_competence (coeff, besoin_id, competence_id) VALUES
(3, 1, 1), -- Java
(2, 1, 2); -- Spring Boot

-- Besoin 2 (Développeur Python) -> Python (ajouter si nécessaire dans competence)
-- Besoin 3 (Data Analyst) -> SQL, Gestion de projet
INSERT INTO besoin_competence (coeff, besoin_id, competence_id) VALUES
(3, 3, 3), -- SQL
(2, 3, 4); -- Gestion de projet

-- Besoin langues
INSERT INTO besoin_langue (coeff, besoin_id, langue_id) VALUES
(2, 1, 1), -- Français
(1, 1, 2), -- Anglais
(2, 3, 2); -- Anglais pour Data Analyst

-- Besoin diplômes / filières
-- Besoin 1 -> Licence Informatique ou Master Informatique
INSERT INTO besoin_diplome_filiere (coeff, besoin_id, diplome_filiere_id) VALUES
(3, 1, 1), 
(3, 1, 2);

-- Besoin 3 -> Licence Gestion ou Master Management
INSERT INTO besoin_diplome_filiere (coeff, besoin_id, diplome_filiere_id) VALUES
(3, 3, 3), 
(3, 3, 4);
