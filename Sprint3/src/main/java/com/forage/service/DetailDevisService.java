package com.forage.service;

import com.forage.entity.DetailDevis;
import com.forage.repository.DetailDevisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
                .map(DetailDevis::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
