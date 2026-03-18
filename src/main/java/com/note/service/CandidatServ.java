package com.note.service;

import com.note.model.Candidat;
import com.note.repository.CandidatRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidatServ {

    @Autowired
    private CandidatRepo candidatRepo;

    public List<Candidat> findAll() {
        return candidatRepo.findAll();
    }

    public Candidat save(Candidat candidat) {
        return candidatRepo.save(candidat);
    }

    public Optional<Candidat> findById(Long id) {
        return candidatRepo.findById(id);
    }

    public Candidat update(Long id, Candidat candidatDetails) {
        Optional<Candidat> candidatOpt = candidatRepo.findById(id);
        if (candidatOpt.isPresent()) {
            Candidat candidat = candidatOpt.get();
            candidat.setNom(candidatDetails.getNom());
            candidat.setPrenom(candidatDetails.getPrenom());
            candidat.setAdresse(candidatDetails.getAdresse());
            candidat.setLienPhoto(candidatDetails.getLienPhoto());
            return candidatRepo.save(candidat);
        }
        return null;
    }

    public void delete(Long id) {
        candidatRepo.deleteById(id);
    }

    
}