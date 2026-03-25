package com.forage.service;

import com.forage.entity.Paiement;
import com.forage.repository.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PaiementService {

    @Autowired
    private PaiementRepository paiementRepository;

    public List<Paiement> findAll() {
        return paiementRepository.findAll();
    }

    public Optional<Paiement> findById(Long id) {
        return paiementRepository.findById(id);
    }

    public Paiement save(Paiement paiement) {
        return paiementRepository.save(paiement);
    }

    public void deleteById(Long id) {
        paiementRepository.deleteById(id);
    }

    public List<Paiement> findByDevisId(Long devisId) {
        return paiementRepository.findByDevisId(devisId);
    }

    public List<Paiement> findByModePaiement(String modePaiement) {
        return paiementRepository.findByModePaiement(modePaiement);
    }

    public BigDecimal calculerTotalPaiements(Long devisId) {
        List<Paiement> paiements = paiementRepository.findByDevisId(devisId);
        return paiements.stream()
                .map(Paiement::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
