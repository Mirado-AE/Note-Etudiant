package com.note.service;

import com.note.model.Correcteur;
import com.note.repository.CorrecteurRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CorrecteurServ {

    @Autowired
    private CorrecteurRepo correcteurRepo;

    public List<Correcteur> findAll() {
        return correcteurRepo.findAll();
    }

    public Correcteur save(Correcteur correcteur) {
        return correcteurRepo.save(correcteur);
    }

    public Optional<Correcteur> findById(Long id) {
        return correcteurRepo.findById(id);
    }

    public Correcteur update(Long id, Correcteur correcteurDetails) {
        Optional<Correcteur> correcteurOpt = correcteurRepo.findById(id);
        if (correcteurOpt.isPresent()) {
            Correcteur correcteur = correcteurOpt.get();
            correcteur.setNom(correcteurDetails.getNom());
            correcteur.setPrenom(correcteurDetails.getPrenom());
            correcteur.setAdresse(correcteurDetails.getAdresse());
            correcteur.setLienPhoto(correcteurDetails.getLienPhoto());
            return correcteurRepo.save(correcteur);
        }
        return null;
    }

    public void delete(Long id) {
        correcteurRepo.deleteById(id);
    }

    
}