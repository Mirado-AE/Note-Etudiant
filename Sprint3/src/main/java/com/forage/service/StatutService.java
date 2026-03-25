package com.forage.service;

import com.forage.entity.Statut;
import com.forage.repository.StatutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StatutService {

    @Autowired
    private StatutRepository statutRepository;

    public List<Statut> findAll() {
        return statutRepository.findAll();
    }

    public Optional<Statut> findById(Long id) {
        return statutRepository.findById(id);
    }

    public Statut save(Statut statut) {
        return statutRepository.save(statut);
    }

    public void deleteById(Long id) {
        statutRepository.deleteById(id);
    }

    public List<Statut> searchByLibelle(String libelle) {
        return statutRepository.findByLibelleContainingIgnoreCase(libelle);
    }
}
