package com.note.repository;

import com.note.model.Examen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamenRepo extends JpaRepository<Examen, Long> {
    
}