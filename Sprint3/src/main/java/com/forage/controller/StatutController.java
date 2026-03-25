package com.forage.controller;

import com.forage.entity.Statut;
import com.forage.service.StatutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/statuts")
public class StatutController {

    @Autowired
    private StatutService statutService;

    @GetMapping
    public String listStatuts(Model model) {
        model.addAttribute("statuts", statutService.findAll());
        return "statuts/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("statut", new Statut());
        return "statuts/form";
    }

    @PostMapping
    public String saveStatut(@ModelAttribute Statut statut) {
        statutService.save(statut);
        return "redirect:/statuts";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("statut", statutService.findById(id).orElse(null));
        return "statuts/form";
    }

    @PostMapping("/update/{id}")
    public String updateStatut(@PathVariable Long id, @ModelAttribute Statut statut) {
        statut.setId(id);
        statutService.save(statut);
        return "redirect:/statuts";
    }

    @GetMapping("/delete/{id}")
    public String deleteStatut(@PathVariable Long id) {
        statutService.deleteById(id);
        return "redirect:/statuts";
    }
}
