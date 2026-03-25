CREATE DATABASE gestion_forage;
\c gestion_forage;

DROP TABLE IF EXISTS paiement CASCADE;
DROP TABLE IF EXISTS detail_devis CASCADE;
DROP TABLE IF EXISTS devis CASCADE;
DROP TABLE IF EXISTS type_devis CASCADE;
DROP TABLE IF EXISTS statut CASCADE;
DROP TABLE IF EXISTS demande CASCADE;
DROP TABLE IF EXISTS client CASCADE;
DROP TABLE IF EXISTS commune CASCADE;
DROP TABLE IF EXISTS district CASCADE;
DROP TABLE IF EXISTS region CASCADE;

CREATE TABLE client (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    contact VARCHAR(100),
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE region (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL
);

CREATE TABLE district (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    id_region INT REFERENCES region(id)
);

CREATE TABLE commune (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    id_district INT REFERENCES district(id)
);

CREATE TABLE demande (
    id SERIAL PRIMARY KEY,
    date_demande TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    id_client INT REFERENCES client(id),

    id_region INT REFERENCES region(id),
    id_district INT REFERENCES district(id),
    id_commune INT REFERENCES commune(id)
);

CREATE TABLE statut (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL
);

INSERT INTO statut (libelle) VALUES
('FORAGE_COMMENCE'),
('FORAGE_SUSPENDU'),
('FORAGE_TERMINE');

CREATE TABLE type_devis (
    id SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL
);

INSERT INTO type_devis (libelle) VALUES
('ETUDE'),
('MATERIAUX');

CREATE TABLE devis (
    id SERIAL PRIMARY KEY,
    montant_total DECIMAL(12,2) NOT NULL,
    date_devis TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    id_demande INT REFERENCES demande(id),
    id_type_devis INT REFERENCES type_devis(id),
    id_statut INT REFERENCES statut(id),

    statut_paiement VARCHAR(50) DEFAULT 'NON_PAYE'
);

CREATE TABLE detail_devis (
    id SERIAL PRIMARY KEY,
    id_devis INT REFERENCES devis(id),
    libelle VARCHAR(150),
    montant DECIMAL(12,2)
);

CREATE TABLE paiement (
    id SERIAL PRIMARY KEY,
    montant DECIMAL(12,2) NOT NULL,
    date_paiement TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    mode_paiement VARCHAR(50),
    reference VARCHAR(100),

    id_devis INT REFERENCES devis(id)
);