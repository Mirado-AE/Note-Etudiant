package com.forage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.forage.entity.Demande;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Long> {
    
    List<Demande> findByClientId(Long clientId);
    
    List<Demande> findByRegionId(Long regionId);
    
    List<Demande> findByDistrictId(Long districtId);
    
    List<Demande> findByCommuneId(Long communeId);
    
    List<Demande> findByStatutId(Long statutId);

    @Query(value = "SELECT " +
                   "s.libelle as statut, " +
                   "COUNT(d.id) as nombre_demandes " +
                   "FROM demandes d " +
                   "JOIN statut s ON d.id_statut = s.id " +
                   "WHERE s.libelle IN ('Créée', 'Validée', 'Rejetée') " +
                   "GROUP BY s.id, s.libelle " +
                   "ORDER BY CASE WHEN s.libelle = 'Créée' THEN 1 WHEN s.libelle = 'Validée' THEN 2 ELSE 3 END",
           nativeQuery = true)
    List<Object[]> getStatistiquesDemandesParStatut();
}
