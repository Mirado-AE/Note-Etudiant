package com.note.service.parametre;

import com.note.model.parametre.Parametre;
import com.note.repository.parametre.ParametreRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParametreServ {

    @Autowired
    private ParametreRepo parametreRepo;

    public List<Parametre> findAll() {
        return parametreRepo.findAll();
    }

    public Parametre save(Parametre parametre) {
        return parametreRepo.save(parametre);
    }

    public Optional<Parametre> findById(Long id) {
        return parametreRepo.findById(id);
    }

    public Parametre update(Long id, Parametre parametreDetails) {
        Optional<Parametre> parametreOpt = parametreRepo.findById(id);
        if (parametreOpt.isPresent()) {
            Parametre parametre = parametreOpt.get();
            parametre.setMatiere(parametreDetails.getMatiere());
            parametre.setOperateur(parametreDetails.getOperateur());
            parametre.setSeuilSumDiff(parametreDetails.getSeuilSumDiff());
            parametre.setResolution(parametreDetails.getResolution());
            return parametreRepo.save(parametre);
        }
        return null;
    }

    public void delete(Long id) {
        parametreRepo.deleteById(id);
    }
}