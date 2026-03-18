package com.note.controller;

import com.note.model.Correcteur;
import com.note.service.CorrecteurServ;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/correcteur")
public class CorrecteurCtrl {

    @Autowired
    private CorrecteurServ correcteurServ;

    @GetMapping
    public String pageCorrecteur(Model model) {
        List<Correcteur> correcteurs = correcteurServ.findAll();
        model.addAttribute("correcteurs", correcteurs);
        
        if (!model.containsAttribute("correcteur")) {
            model.addAttribute("correcteur", new Correcteur());
        }
        
        model.addAttribute("activePage", "correcteur");
        return "correcteur/correcteur";
    }

    @PostMapping("/save")
    public String saveCorrecteur(@ModelAttribute Correcteur correcteur, 
                                 @RequestParam(value = "id", required = false) Long id,
                                 RedirectAttributes redirectAttributes) {
        try {
            if (id != null && id > 0) {
                Correcteur updated = correcteurServ.update(id, correcteur);
                if (updated != null) {
                    redirectAttributes.addFlashAttribute("success", "Correcteur modifié avec succès");
                } else {
                    redirectAttributes.addFlashAttribute("error", "Correcteur non trouvé");
                }
            } else {
                correcteurServ.save(correcteur);
                redirectAttributes.addFlashAttribute("success", "Correcteur ajouté avec succès");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'enregistrement");
        }
        return "redirect:/correcteur";
    }

    @GetMapping("/edit/{id}")
    public String editCorrecteur(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Correcteur> correcteurOpt = correcteurServ.findById(id);
        if (correcteurOpt.isPresent()) {
            List<Correcteur> correcteurs = correcteurServ.findAll();
            model.addAttribute("correcteurs", correcteurs);
            model.addAttribute("correcteur", correcteurOpt.get());
            model.addAttribute("activePage", "correcteur");
            return "correcteur/correcteur";
        } else {
            redirectAttributes.addFlashAttribute("error", "Correcteur non trouvé");
            return "redirect:/correcteur";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCorrecteur(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Correcteur> correcteurOpt = correcteurServ.findById(id);
            if (correcteurOpt.isPresent()) {
                correcteurServ.delete(id);
                redirectAttributes.addFlashAttribute("success", "Correcteur supprimé avec succès");
            } else {
                redirectAttributes.addFlashAttribute("error", "Correcteur non trouvé");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Impossible de supprimer le correcteur");
        }
        return "redirect:/correcteur";
    }

    @GetMapping("/cancel")
    public String cancelEdit() {
        return "redirect:/correcteur";
    }
}