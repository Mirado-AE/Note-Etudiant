package com.note.controller;

import com.note.model.Candidat;
import com.note.model.Matiere;
import com.note.model.Examen;
import com.note.model.Note;
import com.note.service.CandidatServ;
import com.note.service.MatiereServ;
import com.note.service.ExamenServ;
import com.note.service.NoteServ;
import com.note.service.NoteFinalServ;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeCtrl {

    @Autowired
    private CandidatServ candidatServ;

    @Autowired
    private MatiereServ matiereServ;

    @Autowired
    private ExamenServ examenServ;

    @Autowired
    private NoteServ noteServ;

    @Autowired
    private NoteFinalServ noteFinalServ;

    @GetMapping("/")
    public String index(Model model) {
        List<Candidat> candidats = candidatServ.findAll();
        List<Matiere> matieres = matiereServ.findAll();
        List<Examen> examens = examenServ.findAll();
        
        model.addAttribute("candidats", candidats);
        model.addAttribute("matieres", matieres);
        model.addAttribute("examens", examens);
        model.addAttribute("activePage", "accueil");
        
        return "index";
    }

    @PostMapping("/calculer")
    public String calculerNote(
            @RequestParam("candidatId") Long candidatId,
            @RequestParam("matiereId") Long matiereId,
            @RequestParam("examenId") Long examenId,
            Model model) {
        
        List<Candidat> candidats = candidatServ.findAll();
        List<Matiere> matieres = matiereServ.findAll();
        List<Examen> examens = examenServ.findAll();
        
        model.addAttribute("candidats", candidats);
        model.addAttribute("matieres", matieres);
        model.addAttribute("examens", examens);
        
        Candidat candidat = candidatServ.findById(candidatId).orElse(null);
        Matiere matiere = matiereServ.findById(matiereId).orElse(null);
        Examen examen = examenServ.findById(examenId).orElse(null);
        
        if (candidat != null && matiere != null && examen != null) {
            List<Note> notes = noteServ.getNotesByCandidatIdAndMatiereIdAndExamenId(
                    candidatId, matiereId, examenId
            );
            
            double noteFinale = noteFinalServ.getNoteFinalByMatiere(candidatId, matiereId, examenId);
            
            model.addAttribute("notes", notes);
            model.addAttribute("noteFinale", noteFinale);
            model.addAttribute("candidatNom", candidat.getPrenom() + " " + candidat.getNom());
            model.addAttribute("matiereNom", matiere.getNom());
            model.addAttribute("examenNom", examen.getNom());
            model.addAttribute("candidatId", candidatId);
            model.addAttribute("matiereId", matiereId);
            model.addAttribute("examenId", examenId);
        } else {
            model.addAttribute("error", "Veuillez sélectionner tous les champs");
        }
        
        model.addAttribute("activePage", "accueil");
        return "index";
    }
}