package com.forage.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forage.entity.DetailDevis;
import com.forage.repository.DetailDevisRepository;

@Service
@Transactional
public class DetailDevisService {

    @Autowired
    private DetailDevisRepository detailDevisRepository;

    public List<DetailDevis> findAll() {
        return detailDevisRepository.findAll();
    }

    public Optional<DetailDevis> findById(Long id) {
        return detailDevisRepository.findById(id);
    }

    public DetailDevis save(DetailDevis detailDevis) {
        // Appliquer la remise automatique avant de sauvegarder
        appliquerRemiseAutomatique(detailDevis);
        return detailDevisRepository.save(detailDevis);
    }

    public void deleteById(Long id) {
        detailDevisRepository.deleteById(id);
    }

    public List<DetailDevis> findByDevisId(Long devisId) {
        return detailDevisRepository.findByDevisId(devisId);
    }

    public BigDecimal calculerTotalDetails(Long devisId) {
        List<DetailDevis> details = detailDevisRepository.findByDevisId(devisId);
        return details.stream()
                .map(DetailDevis::getSousTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcule et applique la remise automatique de 10% si le prix unitaire >= 1.000.000 Ar
     * @param detailDevis le détail du devis
     */
    public void appliquerRemiseAutomatique(DetailDevis detailDevis) {
        if (detailDevis.getPrixUnitaire() != null && detailDevis.getPrixUnitaire().compareTo(new BigDecimal("1000000")) >= 0) {
            // Appliquer 10% de remise unitaire sur les détails de devis dont le prix unitaire >= 1.000.000 Ar
            BigDecimal remise = detailDevis.getPrixUnitaire()
                    .multiply(new BigDecimal("0.10"));
            detailDevis.setRemiseUnitaire(remise);
        } else {
            detailDevis.setRemiseUnitaire(BigDecimal.ZERO);
        }
    }

    /**
     * Recalcule les remises automatiques pour tous les détails d'un devis
     * @param devisId l'ID du devis
     */
    public void recalculerRemisesAutomatiques(Long devisId) {
        List<DetailDevis> details = findByDevisId(devisId);
        for (DetailDevis detail : details) {
            appliquerRemiseAutomatique(detail);
            detailDevisRepository.save(detail);
        }
    }
}
