package com.note.repository.parametre;

import com.note.model.parametre.Operateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperateurRepo extends JpaRepository<Operateur, Long> {
    
}