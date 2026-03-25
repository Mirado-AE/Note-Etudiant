package com.forage.repository;

import com.forage.entity.Demande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Long> {
    
    List<Demande> findByClientId(Long clientId);
    
    List<Demande> findByRegionId(Long regionId);
    
    List<Demande> findByDistrictId(Long districtId);
    
    List<Demande> findByCommuneId(Long communeId);
}
