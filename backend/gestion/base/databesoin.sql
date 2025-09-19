-- =====================
-- COMPÉTENCES
-- =====================
INSERT INTO competence(libelle) VALUES
('Programmation Java'),
('Analyse de données'),
('Gestion de projet'),
('Communication'),
('Service client');

-- =====================
-- DIPLÔMES
-- =====================
INSERT INTO diplome(libelle) VALUES
('Licence'),
('Master'),
('Doctorat');

-- =====================
-- FILIÈRES
-- =====================
INSERT INTO filiere(libelle) VALUES
('Informatique'),
('Gestion'),
('Communication'),
('Statistique');

-- =====================
-- DIPLÔME x FILIÈRE
-- =====================
INSERT INTO diplome_filiere(filiere_id, diplome_id) VALUES
(1, 1), -- Licence Informatique
(1, 2), -- Master Informatique
(2, 1), -- Licence Gestion
(2, 2), -- Master Gestion
(3, 1), -- Licence Communication
(4, 2); -- Master Statistique

-- =====================
-- LANGUES
-- =====================
INSERT INTO langue(libelle) VALUES
('Français'),
('Anglais'),
('Espagnol');

-- =====================
-- DÉPARTEMENTS
-- =====================
INSERT INTO departement(libelle) VALUES
('Informatique'),
('Ressources Humaines'),
('Marketing'),
('Finance');

-- =====================
-- MÉTIERS
-- =====================
INSERT INTO metier(libelle) VALUES
('Développeur Java'),
('Data Analyst'),
('Chargé RH'),
('Community Manager'),
('Comptable');

-- =====================
-- BESOINS (Annonces)
-- =====================
-- 1. Besoin d’un développeur Java dans le département Informatique
INSERT INTO besoin(statut, min_age, max_age, min_experience, nb_poste_dsipo,
                   coeff_age, coeff_experience, metier_id, departement_id)
VALUES (1, 22, 35, 2, 3, 2, 3, 1, 1);

-- 2. Besoin d’un Data Analyst dans le département Finance
INSERT INTO besoin(statut, min_age, max_age, min_experience, nb_poste_dsipo,
                   coeff_age, coeff_experience, metier_id, departement_id)
VALUES (1, 25, 40, 3, 2, 2, 4, 2, 4);

-- 3. Besoin d’un Community Manager en Marketing
INSERT INTO besoin(statut, min_age, max_age, min_experience, nb_poste_dsipo,
                   coeff_age, coeff_experience, metier_id, departement_id)
VALUES (1, 20, 30, 1, 1, 1, 2, 4, 3);

-- =====================
-- COMPÉTENCES REQUISES
-- =====================
-- Développeur Java
INSERT INTO besoin_competence(coeff, besoin_id, competence_id) VALUES
(5, 1, 1), -- Programmation Java
(3, 1, 3); -- Gestion de projet

-- Data Analyst
INSERT INTO besoin_competence(coeff, besoin_id, competence_id) VALUES
(4, 2, 2), -- Analyse de données
(2, 2, 4); -- Communication

-- Community Manager
INSERT INTO besoin_competence(coeff, besoin_id, competence_id) VALUES
(3, 3, 4), -- Communication
(4, 3, 5); -- Service client

-- =====================
-- LANGUES REQUISES
-- =====================
INSERT INTO besoin_langue(coeff, besoin_id, langue_id) VALUES
(5, 1, 1), -- Français obligatoire pour Dev Java
(4, 1, 2), -- Anglais apprécié
(5, 2, 1), -- Français obligatoire pour Data Analyst
(4, 2, 2), -- Anglais obligatoire
(3, 3, 1), -- Français pour Community Manager
(2, 3, 3); -- Espagnol apprécié

-- =====================
-- DIPLÔMES REQUIS
-- =====================
INSERT INTO besoin_diplome_filiere(coeff, besoin_id, diplome_filiere_id) VALUES
(5, 1, 2), -- Master Informatique pour Dev Java
(4, 2, 6), -- Master Statistique pour Data Analyst
(3, 3, 5); -- Licence Communication pour Community Manager

Invoke-RestMethod -Uri "http://localhost:8080/api/besoins" `
  -Method POST `
  -ContentType "application/json" `
  -Body '{
    "statut": 1,
    "minAge": 22,
    "maxAge": 35,
    "nbPosteDispo": 3,
    "minExperience": 2,
    "coeffAge": 2,
    "coeffExperience": 3,
    "metierId": 1,
    "departementId": 2,
    "competenceIds": [1,2],
    "langueIds": [1],
    "diplomeFiliereIds": [1]
  }'

$body = '{
  "statut": 1,
  "minAge": 22,
  "maxAge": 35,
  "nbPosteDispo": 3,
  "minExperience": 2,
  "coeffAge": 2,
  "coeffExperience": 3,
  "metierId": 1,
  "departementId": 2,
  "competenceIds": [1,2],
  "langueIds": [1],
  "diplomeFiliereIds": [1]
}'

Invoke-RestMethod -Uri "http://localhost:8080/api/besoins" `
  -Method POST `
  -ContentType "application/json" `
  -Body $body
