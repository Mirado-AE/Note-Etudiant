package com.note.controller;

import com.note.model.Candidat;
import com.note.service.CandidatServ;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/candidat")
public class CandidatCtrl {

    @Autowired
    private CandidatServ candidatServ;

    @GetMapping
    public String pageCandidat(Model model) {
        List<Candidat> candidats = candidatServ.findAll();
        model.addAttribute("candidats", candidats);
        
        if (!model.containsAttribute("candidat")) {
            model.addAttribute("candidat", new Candidat());
        }
        
        model.addAttribute("activePage", "candidat");
        return "candidat/candidat";
    }

    @PostMapping("/save")
    public String saveCandidat(@ModelAttribute Candidat candidat, 
                               @RequestParam(value = "id", required = false) Long id,
                               RedirectAttributes redirectAttributes) {
        try {
            if (id != null && id > 0) {
                Candidat updated = candidatServ.update(id, candidat);
                if (updated != null) {
                    redirectAttributes.addFlashAttribute("success", "Candidat modifié avec succès");
                } else {
                    redirectAttributes.addFlashAttribute("error", "Candidat non trouvé");
                }
            } else {
                candidatServ.save(candidat);
                redirectAttributes.addFlashAttribute("success", "Candidat ajouté avec succès");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'enregistrement");
        }
        return "redirect:/candidat";
    }

    @GetMapping("/edit/{id}")
    public String editCandidat(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Candidat> candidatOpt = candidatServ.findById(id);
        if (candidatOpt.isPresent()) {
            List<Candidat> candidats = candidatServ.findAll();
            model.addAttribute("candidats", candidats);
            model.addAttribute("candidat", candidatOpt.get());
            model.addAttribute("activePage", "candidat");
            return "candidat/candidat";
        } else {
            redirectAttributes.addFlashAttribute("error", "Candidat non trouvé");
            return "redirect:/candidat";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCandidat(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Candidat> candidatOpt = candidatServ.findById(id);
            if (candidatOpt.isPresent()) {
                candidatServ.delete(id);
                redirectAttributes.addFlashAttribute("success", "Candidat supprimé avec succès");
            } else {
                redirectAttributes.addFlashAttribute("error", "Candidat non trouvé");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Impossible de supprimer le candidat car il est référencé par d'autres enregistrements");
        }
        return "redirect:/candidat";
    }

    @GetMapping("/cancel")
    public String cancelEdit() {
        return "redirect:/candidat";
    }
    
}