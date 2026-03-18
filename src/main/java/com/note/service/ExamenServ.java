package com.note.service;

import com.note.model.Examen;
import com.note.repository.ExamenRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamenServ {

    @Autowired
    private ExamenRepo examenRepo;
    
    public List<Examen> findAll(){
        return examenRepo.findAll();
    }

    public Optional<Examen> findById(Long id) {
        return examenRepo.findById(id);
    }
}