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
