package com.forage.controller;

import com.forage.entity.Devis;
import com.forage.service.DevisService;
import com.forage.service.DemandeService;
import com.forage.service.TypeDevisService;
import com.forage.service.StatutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/devis")
public class DevisController {

    @Autowired
    private DevisService devisService;

    @Autowired
    private DemandeService demandeService;

    @Autowired
    private TypeDevisService typeDevisService;

    @Autowired
    private StatutService statutService;

    @GetMapping
    public String listDevis(Model model) {
        model.addAttribute("devis", devisService.findAll());
        return "devis/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("devis", new Devis());
        model.addAttribute("demandes", demandeService.findAll());
        model.addAttribute("typesDevis", typeDevisService.findAll());
        model.addAttribute("statuts", statutService.findAll());
        return "devis/form";
    }

    @PostMapping
    public String saveDevis(@ModelAttribute Devis devis) {
        devisService.save(devis);
        return "redirect:/devis";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("devis", devisService.findById(id).orElse(null));
        model.addAttribute("demandes", demandeService.findAll());
        model.addAttribute("typesDevis", typeDevisService.findAll());
        model.addAttribute("statuts", statutService.findAll());
        return "devis/form";
    }

    @PostMapping("/update/{id}")
    public String updateDevis(@PathVariable Long id, @ModelAttribute Devis devis) {
        devis.setId(id);
        devisService.save(devis);
        return "redirect:/devis";
    }

    @GetMapping("/delete/{id}")
    public String deleteDevis(@PathVariable Long id) {
        devisService.deleteById(id);
        return "redirect:/devis";
    }

    @GetMapping("/by-demande/{demandeId}")
    public String getByDemande(@PathVariable Long demandeId, Model model) {
        model.addAttribute("devis", devisService.findByDemandeId(demandeId));
        return "devis/list";
    }

    @GetMapping("/by-statut/{statutId}")
    public String getByStatut(@PathVariable Long statutId, Model model) {
        model.addAttribute("devis", devisService.findByStatutId(statutId));
        return "devis/list";
    }
}
