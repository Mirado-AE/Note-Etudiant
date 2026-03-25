package com.forage.service;

import com.forage.entity.Devis;
import com.forage.repository.DevisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DevisService {

    @Autowired
    private DevisRepository devisRepository;

    public List<Devis> findAll() {
        return devisRepository.findAll();
    }

    public Optional<Devis> findById(Long id) {
        return devisRepository.findById(id);
    }

    public Devis save(Devis devis) {
        return devisRepository.save(devis);
    }

    public void deleteById(Long id) {
        devisRepository.deleteById(id);
    }

    public List<Devis> findByDemandeId(Long demandeId) {
        return devisRepository.findByDemandeId(demandeId);
    }

    public List<Devis> findByTypeDevisId(Long typeDevisId) {
        return devisRepository.findByTypeDevisId(typeDevisId);
    }

    public List<Devis> findByStatutId(Long statutId) {
        return devisRepository.findByStatutId(statutId);
    }

    public List<Devis> findByStatutPaiement(String statutPaiement) {
        return devisRepository.findByStatutPaiement(statutPaiement);
    }

    public BigDecimal calculerMontantTotal(Long devisId) {
        Optional<Devis> devis = devisRepository.findById(devisId);
        if (devis.isPresent()) {
            return devis.get().getMontantTotal();
        }
        return BigDecimal.ZERO;
    }
}
