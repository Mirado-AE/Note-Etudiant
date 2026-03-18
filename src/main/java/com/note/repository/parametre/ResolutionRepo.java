package com.note.repository.parametre;

import com.note.model.parametre.Resolution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResolutionRepo extends JpaRepository<Resolution, Long> {
    
}