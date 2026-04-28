CREATE DATABASE gestion_forage;
\c gestion_forage;

DROP TABLE IF EXISTS paiement CASCADE;
DROP TABLE IF EXISTS details_devis CASCADE;
DROP TABLE IF EXISTS devis CASCADE;
DROP TABLE IF EXISTS demandes CASCADE;
DROP TABLE IF EXISTS commune CASCADE;
DROP TABLE IF EXISTS district CASCADE;
DROP TABLE IF EXISTS produits CASCADE;
DROP TABLE IF EXISTS clients CASCADE;
DROP TABLE IF EXISTS region CASCADE;
DROP TABLE IF EXISTS type_devis CASCADE;
DROP TABLE IF EXISTS statut CASCADE;

DROP SEQUENCE IF EXISTS seq_statut;
DROP SEQUENCE IF EXISTS seq_type_devis;
DROP SEQUENCE IF EXISTS seq_clients;
DROP SEQUENCE IF EXISTS seq_region;
DROP SEQUENCE IF EXISTS seq_district;
DROP SEQUENCE IF EXISTS seq_commune;
DROP SEQUENCE IF EXISTS seq_produits;
DROP SEQUENCE IF EXISTS seq_demandes;
DROP SEQUENCE IF EXISTS seq_devis;
DROP SEQUENCE IF EXISTS seq_details_devis;
DROP SEQUENCE IF EXISTS seq_paiement;

CREATE SEQUENCE seq_statut START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_type_devis START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_clients START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_region START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_district START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_commune START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_produits START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_demandes START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_devis START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_details_devis START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_paiement START WITH 1 INCREMENT BY 1;

CREATE TABLE statut (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_statut'),
    libelle VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_statut_libelle ON statut(libelle);

CREATE TABLE type_devis (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_type_devis'),
    libelle VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_type_devis_libelle ON type_devis(libelle);

CREATE TABLE clients (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_clients'),
    nom VARCHAR(150) NOT NULL,
    email VARCHAR(150) UNIQUE,
    telephone VARCHAR(20),
    adresse TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_clients_nom ON clients(nom);
CREATE INDEX idx_clients_email ON clients(email);

CREATE TABLE region (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_region'),
    nom VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_region_nom ON region(nom);

CREATE TABLE district (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_district'),
    nom VARCHAR(100) NOT NULL,
    id_region BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(nom, id_region),
    CONSTRAINT fk_district_region FOREIGN KEY (id_region) REFERENCES region(id) ON DELETE CASCADE
);

CREATE INDEX idx_district_region ON district(id_region);

CREATE TABLE commune (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_commune'),
    nom VARCHAR(100) NOT NULL,
    id_district BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(nom, id_district),
    CONSTRAINT fk_commune_district FOREIGN KEY (id_district) REFERENCES district(id) ON DELETE CASCADE
);

CREATE INDEX idx_commune_district ON commune(id_district);

CREATE TABLE produits (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_produits'),
    nom VARCHAR(150) NOT NULL,
    description TEXT,
    prix_unitaire NUMERIC(15,2) NOT NULL,
    unite VARCHAR(50) NOT NULL,
    actif BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_produits_actif ON produits(actif);
CREATE INDEX idx_produits_nom ON produits(nom);

CREATE TABLE demandes (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_demandes'),
    date_demande TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_client BIGINT NOT NULL,
    id_region BIGINT NOT NULL,
    id_district BIGINT NOT NULL,
    id_commune BIGINT NOT NULL,
    id_statut BIGINT NOT NULL,
    description TEXT,
    observation TEXT,
    version INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_demandes_client FOREIGN KEY (id_client) REFERENCES clients(id) ON DELETE RESTRICT,
    CONSTRAINT fk_demandes_region FOREIGN KEY (id_region) REFERENCES region(id) ON DELETE RESTRICT,
    CONSTRAINT fk_demandes_district FOREIGN KEY (id_district) REFERENCES district(id) ON DELETE RESTRICT,
    CONSTRAINT fk_demandes_commune FOREIGN KEY (id_commune) REFERENCES commune(id) ON DELETE RESTRICT,
    CONSTRAINT fk_demandes_statut FOREIGN KEY (id_statut) REFERENCES statut(id) ON DELETE RESTRICT
);

CREATE INDEX idx_demandes_client ON demandes(id_client);
CREATE INDEX idx_demandes_region ON demandes(id_region);
CREATE INDEX idx_demandes_district ON demandes(id_district);
CREATE INDEX idx_demandes_commune ON demandes(id_commune);
CREATE INDEX idx_demandes_statut ON demandes(id_statut);
CREATE INDEX idx_demandes_date ON demandes(date_demande);
CREATE INDEX idx_demandes_created ON demandes(created_at);

CREATE TABLE devis (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_devis'),
    date_devis TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_demande BIGINT NOT NULL,
    id_type_devis BIGINT NOT NULL,
    id_statut BIGINT NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_devis_demande FOREIGN KEY (id_demande) REFERENCES demandes(id) ON DELETE CASCADE,
    CONSTRAINT fk_devis_type FOREIGN KEY (id_type_devis) REFERENCES type_devis(id) ON DELETE RESTRICT,
    CONSTRAINT fk_devis_statut FOREIGN KEY (id_statut) REFERENCES statut(id) ON DELETE RESTRICT
);

CREATE INDEX idx_devis_demande ON devis(id_demande);
CREATE INDEX idx_devis_type ON devis(id_type_devis);
CREATE INDEX idx_devis_statut ON devis(id_statut);
CREATE INDEX idx_devis_date ON devis(date_devis);
CREATE INDEX idx_devis_created ON devis(created_at);

CREATE TABLE details_devis (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_details_devis'),
    id_devis BIGINT NOT NULL,
    id_produit BIGINT NOT NULL,
    quantite INTEGER NOT NULL DEFAULT 1,
    prix_unitaire NUMERIC(15,2) NOT NULL,
    remise_unitaire NUMERIC(15,2) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_details_devis FOREIGN KEY (id_devis) REFERENCES devis(id) ON DELETE CASCADE,
    CONSTRAINT fk_details_produit FOREIGN KEY (id_produit) REFERENCES produits(id) ON DELETE RESTRICT
);

CREATE INDEX idx_details_devis ON details_devis(id_devis);
CREATE INDEX idx_details_produit ON details_devis(id_produit);

CREATE TABLE paiement (
    id BIGINT PRIMARY KEY DEFAULT nextval('seq_paiement'),
    id_devis BIGINT,
    montant NUMERIC(15,2) NOT NULL,
    date_paiement TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    mode_paiement VARCHAR(50),
    reference VARCHAR(100),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_paiement_devis FOREIGN KEY (id_devis) REFERENCES devis(id) ON DELETE SET NULL
);

CREATE INDEX idx_paiement_devis ON paiement(id_devis);
CREATE INDEX idx_paiement_date ON paiement(date_paiement);

-- VUE CHIFFRE D'AFFAIRE PRÉVISIONNEL
CREATE OR REPLACE VIEW v_chiffre_affaire_previsionnel AS
SELECT 
    SUM((dd.prix_unitaire - COALESCE(dd.remise_unitaire, 0)) * dd.quantite) as total_chiffre_affaire
FROM details_devis dd
JOIN devis d ON dd.id_devis = d.id
WHERE d.id_statut IN (SELECT id FROM statut WHERE libelle IN ('Accepté', 'En cours', 'Complété'));

-- VUE RÉSUMÉ CHIFFRE D'AFFAIRE PAR DEVIS
CREATE OR REPLACE VIEW v_chiffre_affaire_par_devis AS
SELECT 
    d.id as devis_id,
    d.date_devis,
    dm.id as demande_id,
    c.nom as client_nom,
    td.libelle as type_devis,
    s.libelle as statut,
    SUM((dd.prix_unitaire - COALESCE(dd.remise_unitaire, 0)) * dd.quantite) as montant_total
FROM devis d
JOIN demandes dm ON d.id_demande = dm.id
JOIN clients c ON dm.id_client = c.id
JOIN type_devis td ON d.id_type_devis = td.id
JOIN statut s ON d.id_statut = s.id
LEFT JOIN details_devis dd ON d.id = dd.id_devis
GROUP BY d.id, d.date_devis, dm.id, c.nom, td.libelle, s.libelle
ORDER BY d.date_devis DESC;