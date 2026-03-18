package com.note.repository;

import com.note.model.Note;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepo extends JpaRepository<Note, Long> {
    
    List<Note> findByCandidatIdAndMatiereIdAndExamenId(Long candidatId, Long matiereId, Long examenId);
}