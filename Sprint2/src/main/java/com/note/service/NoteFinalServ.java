package com.note.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.note.model.Note;
import com.note.model.parametre.Parametre;
import com.note.model.parametre.Resolution;
import com.note.repository.parametre.ParametreRepo;

@Service
public class NoteFinalServ {

    @Autowired
    private ParametreRepo parametreRepo;

    @Autowired
    private NoteServ noteServ;

    public double getNoteFinalByMatiere(Long candidatId, Long matiereId, Long examenId) {

        List<Parametre> parametres = parametreRepo.findByMatiereId(matiereId);
        List<Note> notes = noteServ.getNotesByCandidatIdAndMatiereIdAndExamenId(candidatId, matiereId, examenId);

        double sumDiff = noteServ.getSumDiffsNotes(notes);

        List<Parametre> parametresValides = new ArrayList<>();

        // chercher tous les paramètres valides
        for (Parametre parametre : parametres) {

            String op = parametre.getOperateur().getOperateur();
            double seuil = parametre.getSeuilSumDiff();

            boolean valide = switch (op) {
                case ">" -> sumDiff > seuil;
                case ">=" -> sumDiff >= seuil;
                case "<" -> sumDiff < seuil;
                case "<=" -> sumDiff <= seuil;
                case "==" -> sumDiff == seuil;
                default -> false;
            };

            if (valide) {
                parametresValides.add(parametre);
            }
        }

        if (parametresValides.isEmpty()) {
            return 0;
        }

        // chercher le seuil le plus proche
        Parametre meilleur = parametresValides.get(0);
        double meilleureDistance = Math.abs(sumDiff - meilleur.getSeuilSumDiff());

        for (Parametre p : parametresValides) {

            double distance = Math.abs(sumDiff - p.getSeuilSumDiff());

            if (distance < meilleureDistance) {
                meilleur = p;
                meilleureDistance = distance;
            }

            // si égalité -> prendre le plus petit seuil
            else if (distance == meilleureDistance &&
                    p.getSeuilSumDiff() < meilleur.getSeuilSumDiff()) {
                meilleur = p;
            }
        }

        return getNoteByResolution(meilleur.getResolution(), notes);
    }

    private double getNoteByResolution(Resolution resolution, List<Note> notes) {

        String res = resolution.getResolution();

        switch (res) {
            case "plus petit":
                return getNotePlusPetit(notes);
            case "plus grand":
                return getNotePlusGrand(notes);
            case "moyenne":
            default:
                return getNoteMoyenne(notes);
        }
    }

    private Double getNotePlusPetit(List<Note> notes) {
        if (notes == null || notes.isEmpty()) { return 0.0; }
        notes = noteServ.sortNotesReversed(notes);
        return notes.get(notes.size() - 1).getNote();
    }

    private Double getNoteMoyenne(List<Note> notes) {
        if (notes == null || notes.isEmpty()) { return 0.0; }
        double somme = 0.0;
        for (Note note : notes) {
            somme += note.getNote();
        }
        return somme / notes.size();
    }

    private Double getNotePlusGrand(List<Note> notes) {
        if (notes == null || notes.isEmpty()) { return 0.0; }
        List<Note> sortedNotes = noteServ.sortNotesReversed(notes);
        return sortedNotes.get(0).getNote();
    }
}