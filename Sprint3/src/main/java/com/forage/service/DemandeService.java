package com.forage.service;

import com.forage.entity.Demande;
import com.forage.repository.DemandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DemandeService {

    @Autowired
    private DemandeRepository demandeRepository;

    public List<Demande> findAll() {
        return demandeRepository.findAll();
    }

    public Optional<Demande> findById(Long id) {
        return demandeRepository.findById(id);
    }

    public Demande save(Demande demande) {
        return demandeRepository.save(demande);
    }

    public void deleteById(Long id) {
        demandeRepository.deleteById(id);
    }

    public List<Demande> findByClientId(Long clientId) {
        return demandeRepository.findByClientId(clientId);
    }

    public List<Demande> findByRegionId(Long regionId) {
        return demandeRepository.findByRegionId(regionId);
    }

    public List<Demande> findByDistrictId(Long districtId) {
        return demandeRepository.findByDistrictId(districtId);
    }

    public List<Demande> findByCommuneId(Long communeId) {
        return demandeRepository.findByCommuneId(communeId);
    }
}
