package com.forage.repository;

import com.forage.entity.Statut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatutRepository extends JpaRepository<Statut, Long> {
    
    java.util.List<Statut> findByLibelleContainingIgnoreCase(String libelle);
}
