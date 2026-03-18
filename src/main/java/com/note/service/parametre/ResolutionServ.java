package com.note.service.parametre;

import com.note.model.parametre.Resolution;
import com.note.repository.parametre.ResolutionRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResolutionServ {

    @Autowired
    private ResolutionRepo resolutionRepo;
    
    public List<Resolution> findAll() {
        return resolutionRepo.findAll();
    }

    public Optional<Resolution> findById(Long id) {
        return resolutionRepo.findById(id);
    }
}