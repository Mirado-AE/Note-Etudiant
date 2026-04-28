package com.forage.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.forage.entity.DetailDevis;

@Repository
public interface DetailDevisRepository extends JpaRepository<DetailDevis, Long> {
    
    List<DetailDevis> findByDevisId(Long devisId);

    @Query(value = "SELECT SUM((dd.prix_unitaire - COALESCE(dd.remise_unitaire, 0)) * dd.quantite) as total " +
                   "FROM details_devis dd " +
                   "JOIN devis d ON dd.id_devis = d.id " +
                   "WHERE d.id_statut IN (SELECT id FROM statut WHERE libelle IN ('Accepté', 'En cours', 'Complété'))",
           nativeQuery = true)
    BigDecimal getChiffreAffairePrevisionnelTotal();

    @Query(value = "SELECT " +
                   "d.id as devis_id, " +
                   "d.date_devis, " +
                   "dm.id as demande_id, " +
                   "c.nom as client_nom, " +
                   "td.libelle as type_devis, " +
                   "s.libelle as statut, " +
                   "SUM((dd.prix_unitaire - COALESCE(dd.remise_unitaire, 0)) * dd.quantite) as montant_total " +
                   "FROM devis d " +
                   "JOIN demandes dm ON d.id_demande = dm.id " +
                   "JOIN clients c ON dm.id_client = c.id " +
                   "JOIN type_devis td ON d.id_type_devis = td.id " +
                   "JOIN statut s ON d.id_statut = s.id " +
                   "LEFT JOIN details_devis dd ON d.id = dd.id_devis " +
                   "WHERE s.libelle IN ('Accepté', 'En cours', 'Complété') " +
                   "GROUP BY d.id, d.date_devis, dm.id, c.nom, td.libelle, s.libelle " +
                   "ORDER BY d.date_devis DESC",
           nativeQuery = true)
    List<Object[]> getChiffreAffaireParDevis();

    @Query(value = "SELECT " +
                   "s.libelle as statut, " +
                   "COUNT(DISTINCT d.id) as nombre_devis, " +
                   "SUM((dd.prix_unitaire - COALESCE(dd.remise_unitaire, 0)) * dd.quantite) as montant_total " +
                   "FROM statut s " +
                   "LEFT JOIN devis d ON s.id = d.id_statut " +
                   "LEFT JOIN details_devis dd ON d.id = dd.id_devis " +
                   "GROUP BY s.id, s.libelle " +
                   "ORDER BY s.libelle",
           nativeQuery = true)
    List<Object[]> getStatistiquesParStatut();

    @Query(value = "SELECT " +
                   "s.libelle as statut, " +
                   "COUNT(DISTINCT d.id) as nombre_devis " +
                   "FROM devis d " +
                   "JOIN statut s ON d.id_statut = s.id " +
                   "WHERE s.libelle IN ('En cours', 'Annulé', 'Accepté') " +
                   "GROUP BY s.id, s.libelle " +
                   "ORDER BY CASE WHEN s.libelle = 'En cours' THEN 1 WHEN s.libelle = 'Accepté' THEN 2 ELSE 3 END",
           nativeQuery = true)
    List<Object[]> getStatistiquesDevisParStatut();
}
