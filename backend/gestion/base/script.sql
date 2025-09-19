CREATE TABLE competence(
   id SERIAL,
   libelle VARCHAR(50),
   PRIMARY KEY(id)
);

CREATE TABLE diplome(
   id SERIAL,
   libelle VARCHAR(50),
   PRIMARY KEY(id)
);

CREATE TABLE filiere(
   id SERIAL,
   libelle VARCHAR(50),
   PRIMARY KEY(id)
);

CREATE TABLE diplome_filiere(
   id SERIAL,
   filiere_id INTEGER NOT NULL,
   diplome_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(filiere_id) REFERENCES filiere(id),
   FOREIGN KEY(diplome_id) REFERENCES diplome(id)
);

CREATE TABLE langue(
   id SERIAL,
   libelle VARCHAR(50),
   PRIMARY KEY(id)
);

CREATE TABLE departement(
   id SERIAL,
   libelle VARCHAR(50),
   PRIMARY KEY(id)
);

CREATE TABLE metier(
   id SERIAL,
   libelle VARCHAR(50),
   PRIMARY KEY(id)
);

CREATE TABLE evaluation(
   id SERIAL,
   libelle VARCHAR(50),
   coeff INTEGER,
   PRIMARY KEY(id)
);

CREATE TABLE personne(
   id SERIAL,
   nom VARCHAR(50),
   image VARCHAR(50),
   prenom VARCHAR(50),
   email VARCHAR(50),
   date_naissance DATE,
   genre INTEGER,
   ville VARCHAR(50),
   telephone VARCHAR(50),
   PRIMARY KEY(id)
);

CREATE TABLE utilisateur(
   id SERIAL,
   login VARCHAR(50),
   mdp VARCHAR(50),
   departement_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(departement_id) REFERENCES departement(id)
);

CREATE TABLE candidat(
   id SERIAL,
   description TEXT,
   personne_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(personne_id) REFERENCES personne(id)
);

CREATE TABLE candidat_competence(
   id SERIAL,
   competence_id INTEGER NOT NULL,
   candidat_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(competence_id) REFERENCES competence(id),
   FOREIGN KEY(candidat_id) REFERENCES candidat(id)
);

CREATE TABLE candidat_langue(
   id SERIAL,
   langue_id INTEGER NOT NULL,
   candidat_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(langue_id) REFERENCES langue(id),
   FOREIGN KEY(candidat_id) REFERENCES candidat(id)
);

CREATE TABLE candidat_diplome_filiere(
   id SERIAL,
   diplome_filiere_id INTEGER NOT NULL,
   candidat_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(diplome_filiere_id) REFERENCES diplome_filiere(id),
   FOREIGN KEY(candidat_id) REFERENCES candidat(id)
);

CREATE TABLE besoin(
   id SERIAL,
   statut INTEGER,
   min_age INTEGER,
   nb_poste_dsipo INTEGER,
   coeff_age INTEGER,
   coeff_experience INTEGER,
   max_age INTEGER,
   min_experience INTEGER,
   metier_id INTEGER,
   departement_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(metier_id) REFERENCES metier(id),
   FOREIGN KEY(departement_id) REFERENCES departement(id)
);

CREATE TABLE besoin_competence(
   id SERIAL,
   coeff INTEGER,
   besoin_id INTEGER NOT NULL,
   competence_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(besoin_id) REFERENCES besoin(id),
   FOREIGN KEY(competence_id) REFERENCES competence(id)
);

CREATE TABLE besoin_langue(
   id SERIAL,
   coeff INTEGER,
   besoin_id INTEGER NOT NULL,
   langue_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(besoin_id) REFERENCES besoin(id),
   FOREIGN KEY(langue_id) REFERENCES langue(id)
);

CREATE TABLE besoin_diplome_filiere(
   id SERIAL,
   coeff INTEGER,
   besoin_id INTEGER NOT NULL,
   diplome_filiere_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(besoin_id) REFERENCES besoin(id),
   FOREIGN KEY(diplome_filiere_id) REFERENCES diplome_filiere(id)
);

CREATE TABLE candidature(
   id SERIAL,
   statut INTEGER,
   date_candidature TIMESTAMP,
   besoin_id INTEGER NOT NULL,
   candidat_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(besoin_id) REFERENCES besoin(id),
   FOREIGN KEY(candidat_id) REFERENCES candidat(id)
);

CREATE TABLE question(
   id SERIAL,
   intitule VARCHAR(255),
   metier_id INTEGER,
   PRIMARY KEY(id),
   FOREIGN KEY(metier_id) REFERENCES metier(id)
);

CREATE TABLE choix(
   id SERIAL,
   reponse VARCHAR(255),
   coeff INTEGER,
   question_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(question_id) REFERENCES question(id)
);

CREATE TABLE reponse_candidat(
   id SERIAL,
   candidature_id INTEGER NOT NULL,
   choix_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(candidature_id) REFERENCES candidature(id),
   FOREIGN KEY(choix_id) REFERENCES choix(id)
);

CREATE TABLE entretien(
   id SERIAL,
   date_heure_debut TIMESTAMP,
   candidature_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(candidature_id) REFERENCES candidature(id)
);

CREATE TABLE experience(
   id SERIAL,
   nb_annee INTEGER,
   metier_id INTEGER NOT NULL,
   candidat_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(metier_id) REFERENCES metier(id),
   FOREIGN KEY(candidat_id) REFERENCES candidat(id)
);

CREATE TABLE notes(
   id SERIAL,
   note NUMERIC(15,2),
   date_entree TIMESTAMP,
   evaluation_id INTEGER NOT NULL,
   candidature_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(evaluation_id) REFERENCES evaluation(id),
   FOREIGN KEY(candidature_id) REFERENCES candidature(id)
);

CREATE TABLE status_candidature(
   id SERIAL,
   date_entree TIMESTAMP,
   libelle VARCHAR(50),
   candidature_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(candidature_id) REFERENCES candidature(id)
);

CREATE TABLE employe(
   id SERIAL,
   personne_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(personne_id) REFERENCES personne(id)
);

CREATE TABLE status_employe(
   id SERIAL,
   libelle VARCHAR(50),
   date_entree TIMESTAMP,
   employe_id INTEGER NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(employe_id) REFERENCES employe(id)
);