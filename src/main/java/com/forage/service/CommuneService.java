package com.forage.service;

import com.forage.entity.Commune;
import com.forage.repository.CommuneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommuneService {

    @Autowired
    private CommuneRepository communeRepository;

    public List<Commune> findAll() {
        return communeRepository.findAll();
    }

    public Optional<Commune> findById(Long id) {
        return communeRepository.findById(id);
    }

    public Commune save(Commune commune) {
        return communeRepository.save(commune);
    }

    public void deleteById(Long id) {
        communeRepository.deleteById(id);
    }

    public List<Commune> searchByNom(String nom) {
        return communeRepository.findByNomContainingIgnoreCase(nom);
    }

    public List<Commune> findByDistrictId(Long districtId) {
        return communeRepository.findByDistrictId(districtId);
    }
}
