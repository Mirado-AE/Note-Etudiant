package com.forage.repository;

import com.forage.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
    
    List<District> findByNomContainingIgnoreCase(String nom);
    
    List<District> findByRegionId(Long regionId);
}
