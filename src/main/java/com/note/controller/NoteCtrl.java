package com.note.controller;

import com.note.model.*;
import com.note.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/note")
public class NoteCtrl {

    @Autowired
    private NoteServ noteServ;

    @Autowired
    private CorrecteurServ correcteurServ;

    @Autowired
    private CandidatServ candidatServ;

    @Autowired
    private MatiereServ matiereServ;

    @Autowired
    private ExamenServ examenServ;

    @GetMapping
    public String pageNote(Model model) {
        List<Note> notes = noteServ.findAll();
        List<Correcteur> correcteurs = correcteurServ.findAll();
        List<Candidat> candidats = candidatServ.findAll();
        List<Matiere> matieres = matiereServ.findAll();
        List<Examen> examens = examenServ.findAll();
        
        model.addAttribute("notes", notes);
        model.addAttribute("correcteurs", correcteurs);
        model.addAttribute("candidats", candidats);
        model.addAttribute("matieres", matieres);
        model.addAttribute("examens", examens);
        
        if (!model.containsAttribute("note")) {
            model.addAttribute("note", new Note());
        }
        
        model.addAttribute("activePage", "note");
        return "note/note";
    }

    @PostMapping("/save")
    public String saveNote(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam("correcteurId") Long correcteurId,
            @RequestParam("candidatId") Long candidatId,
            @RequestParam("matiereId") Long matiereId,
            @RequestParam("examenId") Long examenId,
            @RequestParam("noteValue") Double noteValue,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Récupérer les objets associés
            Optional<Correcteur> correcteurOpt = correcteurServ.findById(correcteurId);
            Optional<Candidat> candidatOpt = candidatServ.findById(candidatId);
            Optional<Matiere> matiereOpt = matiereServ.findById(matiereId);
            Optional<Examen> examenOpt = examenServ.findById(examenId);
            
            if (!correcteurOpt.isPresent() || !candidatOpt.isPresent() || 
                !matiereOpt.isPresent() || !examenOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Une ou plusieurs références sont invalides");
                return "redirect:/note";
            }
            
            Note note = new Note();
            if (id != null && id > 0) {
                Optional<Note> existingNote = noteServ.findById(id);
                if (existingNote.isPresent()) {
                    note = existingNote.get();
                }
            }
            
            note.setCorrecteur(correcteurOpt.get());
            note.setCandidat(candidatOpt.get());
            note.setMatiere(matiereOpt.get());
            note.setExamen(examenOpt.get());
            note.setNote(noteValue);
            
            if (id != null && id > 0) {
                note.setId(id);
                Note updated = noteServ.update(id, note);
                if (updated != null) {
                    redirectAttributes.addFlashAttribute("success", "Note modifiée avec succès");
                } else {
                    redirectAttributes.addFlashAttribute("error", "Note non trouvée");
                }
            } else {
                noteServ.save(note);
                redirectAttributes.addFlashAttribute("success", "Note ajoutée avec succès");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
        }
        return "redirect:/note";
    }

    @GetMapping("/edit/{id}")
    public String editNote(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Note> noteOpt = noteServ.findById(id);
        if (noteOpt.isPresent()) {
            List<Note> notes = noteServ.findAll();
            List<Correcteur> correcteurs = correcteurServ.findAll();
            List<Candidat> candidats = candidatServ.findAll();
            List<Matiere> matieres = matiereServ.findAll();
            List<Examen> examens = examenServ.findAll();
            
            model.addAttribute("notes", notes);
            model.addAttribute("correcteurs", correcteurs);
            model.addAttribute("candidats", candidats);
            model.addAttribute("matieres", matieres);
            model.addAttribute("examens", examens);
            model.addAttribute("note", noteOpt.get());
            model.addAttribute("activePage", "note");
            return "note/note";
        } else {
            redirectAttributes.addFlashAttribute("error", "Note non trouvée");
            return "redirect:/note";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteNote(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Note> noteOpt = noteServ.findById(id);
            if (noteOpt.isPresent()) {
                noteServ.delete(id);
                redirectAttributes.addFlashAttribute("success", "Note supprimée avec succès");
            } else {
                redirectAttributes.addFlashAttribute("error", "Note non trouvée");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression");
        }
        return "redirect:/note";
    }

    @GetMapping("/cancel")
    public String cancelEdit() {
        return "redirect:/note";
    }
}