package com.note.service;

import com.note.model.Matiere;
import com.note.repository.MatiereRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatiereServ {

    @Autowired
    private MatiereRepo matiereRepo;

    public List<Matiere> findAll() {
        return matiereRepo.findAll();
    }

    public Matiere save(Matiere matiere) {
        return matiereRepo.save(matiere);
    }

    public Optional<Matiere> findById(Long id) {
        return matiereRepo.findById(id);
    }

    public Matiere update(Long id, Matiere matiereDetails) {
        Optional<Matiere> matiereOpt = matiereRepo.findById(id);
        if (matiereOpt.isPresent()) {
            Matiere matiere = matiereOpt.get();
            matiere.setNom(matiereDetails.getNom());
            matiere.setCoef(matiereDetails.getCoef());
            return matiereRepo.save(matiere);
        }
        return null;
    }

    public void delete(Long id) {
        matiereRepo.deleteById(id);
    }
}