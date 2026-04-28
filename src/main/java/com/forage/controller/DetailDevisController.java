package com.forage.controller;

import com.forage.entity.DetailDevis;
import com.forage.service.DetailDevisService;
import com.forage.service.DevisService;
import com.forage.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/details-devis")
public class DetailDevisController {

    @Autowired
    private DetailDevisService detailDevisService;

    @Autowired
    private DevisService devisService;

    @Autowired
    private ProduitService produitService;

    @GetMapping
    public String listDetailsDevis(Model model) {
        model.addAttribute("detailsDevis", detailDevisService.findAll());
        return "details-devis/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("detailDevis", new DetailDevis());
        model.addAttribute("devis", devisService.findAll());
        model.addAttribute("produits", produitService.findAll());
        return "details-devis/form";
    }

    @PostMapping
    public String saveDetailDevis(@ModelAttribute DetailDevis detailDevis) {
        detailDevisService.save(detailDevis);
        return "redirect:/details-devis";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("detailDevis", detailDevisService.findById(id).orElse(null));
        model.addAttribute("devis", devisService.findAll());
        model.addAttribute("produits", produitService.findAll());
        return "details-devis/form";
    }

    @PostMapping("/update/{id}")
    public String updateDetailDevis(@PathVariable Long id, @ModelAttribute DetailDevis detailDevis) {
        detailDevis.setId(id);
        detailDevisService.save(detailDevis);
        return "redirect:/details-devis";
    }

    @GetMapping("/delete/{id}")
    public String deleteDetailDevis(@PathVariable Long id) {
        detailDevisService.deleteById(id);
        return "redirect:/details-devis";
    }

    @GetMapping("/by-devis/{devisId}")
    public String getByDevis(@PathVariable Long devisId, Model model) {
        model.addAttribute("detailsDevis", detailDevisService.findByDevisId(devisId));
        return "details-devis/list";
    }
}
