package com.note.controller;

import com.note.model.Matiere;
import com.note.service.MatiereServ;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/matiere")
public class MatiereCtrl {

    @Autowired
    private MatiereServ matiereServ;

    @GetMapping
    public String pageMatiere(Model model) {
        List<Matiere> matieres = matiereServ.findAll();
        model.addAttribute("matieres", matieres);
        
        if (!model.containsAttribute("matiere")) {
            model.addAttribute("matiere", new Matiere());
        }
        
        model.addAttribute("activePage", "matiere");
        return "matiere/matiere";
    }

    @PostMapping("/save")
    public String saveMatiere(@ModelAttribute Matiere matiere, 
                              @RequestParam(value = "id", required = false) Long id,
                              RedirectAttributes redirectAttributes) {
        try {
            if (id != null && id > 0) {
                Matiere updated = matiereServ.update(id, matiere);
                if (updated != null) {
                    redirectAttributes.addFlashAttribute("success", "Matière modifiée avec succès");
                } else {
                    redirectAttributes.addFlashAttribute("error", "Matière non trouvée");
                }
            } else {
                matiereServ.save(matiere);
                redirectAttributes.addFlashAttribute("success", "Matière ajoutée avec succès");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'enregistrement");
        }
        return "redirect:/matiere";
    }

    @GetMapping("/edit/{id}")
    public String editMatiere(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Matiere> matiereOpt = matiereServ.findById(id);
        if (matiereOpt.isPresent()) {
            List<Matiere> matieres = matiereServ.findAll();
            model.addAttribute("matieres", matieres);
            model.addAttribute("matiere", matiereOpt.get());
            model.addAttribute("activePage", "matiere");
            return "matiere/matiere";
        } else {
            redirectAttributes.addFlashAttribute("error", "Matière non trouvée");
            return "redirect:/matiere";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteMatiere(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Matiere> matiereOpt = matiereServ.findById(id);
            if (matiereOpt.isPresent()) {
                matiereServ.delete(id);
                redirectAttributes.addFlashAttribute("success", "Matière supprimée avec succès");
            } else {
                redirectAttributes.addFlashAttribute("error", "Matière non trouvée");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Impossible de supprimer la matière (peut-être utilisée dans des notes)");
        }
        return "redirect:/matiere";
    }

    @GetMapping("/cancel")
    public String cancelEdit() {
        return "redirect:/matiere";
    }
}