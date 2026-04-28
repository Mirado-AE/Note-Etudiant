package com.forage.controller;

import com.forage.entity.Paiement;
import com.forage.service.PaiementService;
import com.forage.service.DevisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/paiements")
public class PaiementController {

    @Autowired
    private PaiementService paiementService;

    @Autowired
    private DevisService devisService;

    @GetMapping
    public String listPaiements(Model model) {
        model.addAttribute("paiements", paiementService.findAll());
        return "paiements/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("paiement", new Paiement());
        // Only show completed devis in dropdown
        model.addAttribute("devis", devisService.findCompletedDevis());
        return "paiements/form";
    }

    @PostMapping
    public String savePaiement(@ModelAttribute Paiement paiement) {
        paiementService.save(paiement);
        return "redirect:/paiements";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("paiement", paiementService.findById(id).orElse(null));
        // Only show completed devis in dropdown
        model.addAttribute("devis", devisService.findCompletedDevis());
        return "paiements/form";
    }

    @PostMapping("/update/{id}")
    public String updatePaiement(@PathVariable Long id, @ModelAttribute Paiement paiement) {
        paiement.setId(id);
        paiementService.save(paiement);
        return "redirect:/paiements";
    }

    @GetMapping("/delete/{id}")
    public String deletePaiement(@PathVariable Long id) {
        paiementService.deleteById(id);
        return "redirect:/paiements";
    }

    @GetMapping("/by-devis/{devisId}")
    public String getByDevis(@PathVariable Long devisId, Model model) {
        model.addAttribute("paiements", paiementService.findByDevisId(devisId));
        return "paiements/list";
    }
}
