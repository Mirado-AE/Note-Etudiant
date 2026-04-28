package com.forage.repository;

import com.forage.entity.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    
    List<Paiement> findByDevisId(Long devisId);
    
    List<Paiement> findByModePaiement(String modePaiement);
}
