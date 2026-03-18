package com.note.repository.parametre;

import com.note.model.parametre.Parametre;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametreRepo extends JpaRepository<Parametre, Long> {
    public List<Parametre> findByMatiereId(long matiereId);
}