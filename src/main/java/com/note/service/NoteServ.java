package com.note.service;

import com.note.model.Note;
import com.note.repository.NoteRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
// import java.util.Optional;
import java.util.Optional;

@Service
public class NoteServ {

    @Autowired
    private NoteRepo noteRepo;

//////
    public List<Note> getNotesByCandidatIdAndMatiereIdAndExamenId(Long candidatId, Long matiereId, Long examenId) {
        return sortNotesReversed(noteRepo.findByCandidatIdAndMatiereIdAndExamenId(candidatId, matiereId, examenId));
    }

//////
    public Double getSumDiffsNotes(List<Note> notes) {
        List<Double> diffs = getDiffsNotes(notes);
        double sum = 0;
        for (Double diff : diffs) {
            sum += diff;
        }
        return sum;
    }

    private List<Double> getDiffsNotes(List<Note> notes) {
        notes = sortNotesReversed(notes);
        List<Double> diffs = new ArrayList<>();
        for (int i = 0; i < notes.size(); i++) {
            for (int j = i + 1; j < notes.size(); j++) {
                diffs.add(notes.get(i).getNote() - notes.get(j).getNote());
            }
        }
        return diffs;
    }

    public List<Note> sortNotesReversed(List<Note> notes) {
        notes.sort(Comparator.comparingDouble(Note::getNote).reversed());
        return notes;
    }

    
////// CRUD

   public List<Note> findAll() {
        return noteRepo.findAll();
    }

    public Note save(Note note) {
        return noteRepo.save(note);
    }

    public Optional<Note> findById(Long id) {
        return noteRepo.findById(id);
    }

    public Note update(Long id, Note noteDetails) {
        Optional<Note> noteOpt = noteRepo.findById(id);
        if (noteOpt.isPresent()) {
            Note note = noteOpt.get();
            note.setCorrecteur(noteDetails.getCorrecteur());
            note.setCandidat(noteDetails.getCandidat());
            note.setMatiere(noteDetails.getMatiere());
            note.setExamen(noteDetails.getExamen());
            note.setNote(noteDetails.getNote());
            return noteRepo.save(note);
        }
        return null;
    }

    public void delete(Long id) {
        noteRepo.deleteById(id);
    }




}