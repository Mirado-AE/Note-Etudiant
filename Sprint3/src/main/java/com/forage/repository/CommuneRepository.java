package com.forage.repository;

import com.forage.entity.Commune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommuneRepository extends JpaRepository<Commune, Long> {
    
    List<Commune> findByNomContainingIgnoreCase(String nom);
    
    List<Commune> findByDistrictId(Long districtId);
}
