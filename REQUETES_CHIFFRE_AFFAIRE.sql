-- =====================================================
-- REQUÊTES UTILES - CHIFFRE D'AFFAIRE ET HISTORIQUE
-- =====================================================

-- 1. CHIFFRE D'AFFAIRE PRÉVISIONNEL TOTAL
-- Cette requête calcule le montant global prévisionnel de tous les devis acceptés, en cours ou complétés
SELECT SUM((dd.prix_unitaire - COALESCE(dd.remise_unitaire, 0)) * dd.quantite) as total_chiffre_affaire
FROM details_devis dd
JOIN devis d ON dd.id_devis = d.id
WHERE d.id_statut IN (SELECT id FROM statut WHERE libelle IN ('Accepté', 'En cours', 'Complété'));

-- 2. CHIFFRE D'AFFAIRE PAR DEVIS AVEC DÉTAILS
-- Affiche le détail du chiffre d'affaire pour chaque devis
SELECT 
    d.id as devis_id,
    d.date_devis,
    dm.id as demande_id,
    dm.version,
    c.nom as client_nom,
    c.telephone,
    c.email,
    td.libelle as type_devis,
    s.libelle as statut,
    SUM((dd.prix_unitaire - COALESCE(dd.remise_unitaire, 0)) * dd.quantite) as montant_total
FROM devis d
JOIN demandes dm ON d.id_demande = dm.id
JOIN clients c ON dm.id_client = c.id
JOIN type_devis td ON d.id_type_devis = td.id
JOIN statut s ON d.id_statut = s.id
LEFT JOIN details_devis dd ON d.id = dd.id_devis
GROUP BY d.id, d.date_devis, dm.id, dm.version, c.nom, c.telephone, c.email, td.libelle, s.libelle
ORDER BY d.date_devis DESC;

-- 3. CHIFFRE D'AFFAIRE PAR CLIENT
-- Affiche le total de chiffre d'affaire pour chaque client
SELECT 
    c.id,
    c.nom as client_nom,
    c.email,
    c.telephone,
    COUNT(DISTINCT d.id) as nombre_devis,
    SUM((dd.prix_unitaire - COALESCE(dd.remise_unitaire, 0)) * dd.quantite) as montant_total_client
FROM clients c
JOIN demandes dm ON c.id = dm.id_client
JOIN devis d ON dm.id = d.id_demande
LEFT JOIN details_devis dd ON d.id = dd.id_devis
WHERE d.id_statut IN (SELECT id FROM statut WHERE libelle IN ('Accepté', 'En cours', 'Complété'))
GROUP BY c.id, c.nom, c.email, c.telephone
ORDER BY montant_total_client DESC;

-- 4. REMISES ACCORDÉES - ANALYSE
-- Affiche le total des remises accordées (prix >= 1.000.000 Ar)
SELECT 
    COUNT(*) as nombre_lignes_avec_remise,
    SUM(COALESCE(dd.remise_unitaire, 0)) as remise_unitaire_totale,
    SUM(COALESCE(dd.remise_unitaire, 0) * dd.quantite) as remise_totale,
    SUM((dd.prix_unitaire - COALESCE(dd.remise_unitaire, 0)) * dd.quantite) as chiffre_affaire_apres_remise
FROM details_devis dd
JOIN devis d ON dd.id_devis = d.id
WHERE d.id_statut IN (SELECT id FROM statut WHERE libelle IN ('Accepté', 'En cours', 'Complété'))
AND COALESCE(dd.remise_unitaire, 0) > 0;

-- 5. HISTORIQUE DES DEMANDES - VERSIONING
-- Affiche l'historique de modification des demandes (version tracking)
SELECT 
    id,
    id_client,
    date_demande,
    version,
    created_at,
    updated_at,
    description,
    observation,
    CASE 
        WHEN version = 1 THEN 'Création initiale'
        ELSE 'Modification - Version ' || version
    END as type_modification
FROM demandes
ORDER BY id, version DESC;

-- 6. DÉTAILS AVEC REMISE - POUR CHAQUE ARTICLE
-- Affiche le détail de chaque article avec la remise appliquée
SELECT 
    dd.id,
    d.id as devis_id,
    p.nom as produit_nom,
    dd.quantite,
    dd.prix_unitaire,
    dd.remise_unitaire,
    (dd.prix_unitaire - COALESCE(dd.remise_unitaire, 0)) as prix_apres_remise,
    ((dd.prix_unitaire - COALESCE(dd.remise_unitaire, 0)) * dd.quantite) as sous_total
FROM details_devis dd
JOIN devis d ON dd.id_devis = d.id
JOIN produits p ON dd.id_produit = p.id
WHERE d.id_statut IN (SELECT id FROM statut WHERE libelle IN ('Accepté', 'En cours', 'Complété'))
ORDER BY d.id DESC, dd.id;

-- 7. VIEW: CHIFFRE D'AFFAIRE PRÉVISIONNEL
-- Vue pour le chiffre d'affaire prévisionnel total (déjà créée dans le base.sql)
-- CREATE OR REPLACE VIEW v_chiffre_affaire_previsionnel AS
-- SELECT 
--     SUM((dd.prix_unitaire - COALESCE(dd.remise_unitaire, 0)) * dd.quantite) as total_chiffre_affaire
-- FROM details_devis dd
-- JOIN devis d ON dd.id_devis = d.id
-- WHERE d.id_statut IN (SELECT id FROM statut WHERE libelle IN ('Accepté', 'En cours', 'Complété'));

-- 8. SELECT DE LA VIEW
SELECT * FROM v_chiffre_affaire_previsionnel;

-- 9. SELECT DE LA VIEW PAR DEVIS
SELECT * FROM v_chiffre_affaire_par_devis;
