package com.forage.controller;

import com.forage.entity.TypeDevis;
import com.forage.service.TypeDevisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/types-devis")
public class TypeDevisController {

    @Autowired
    private TypeDevisService typeDevisService;

    @GetMapping
    public String listTypesDevis(Model model) {
        model.addAttribute("typesDevis", typeDevisService.findAll());
        return "types-devis/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("typeDevis", new TypeDevis());
        return "types-devis/form";
    }

    @PostMapping
    public String saveTypeDevis(@ModelAttribute TypeDevis typeDevis) {
        typeDevisService.save(typeDevis);
        return "redirect:/types-devis";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("typeDevis", typeDevisService.findById(id).orElse(null));
        return "types-devis/form";
    }

    @PostMapping("/update/{id}")
    public String updateTypeDevis(@PathVariable Long id, @ModelAttribute TypeDevis typeDevis) {
        typeDevis.setId(id);
        typeDevisService.save(typeDevis);
        return "redirect:/types-devis";
    }

    @GetMapping("/delete/{id}")
    public String deleteTypeDevis(@PathVariable Long id) {
        typeDevisService.deleteById(id);
        return "redirect:/types-devis";
    }
}
