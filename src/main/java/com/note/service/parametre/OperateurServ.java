package com.note.service.parametre;

import com.note.model.parametre.Operateur;
import com.note.repository.parametre.OperateurRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OperateurServ {

    @Autowired
    private OperateurRepo operateurRepo;
    
    public List<Operateur> findAll() {
        return operateurRepo.findAll();
    }

    public Optional<Operateur> findById(Long id) {
        return operateurRepo.findById(id);
    }
    
}