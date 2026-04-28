package com.forage.repository;

import com.forage.entity.DemandeHistorique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeHistoriqueRepository extends JpaRepository<DemandeHistorique, Long> {

    List<DemandeHistorique> findByDemandeIdOrderByDateActionDesc(Long demandeId);
}
