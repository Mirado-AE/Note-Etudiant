package com.forage.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.forage.entity.Demande;
import com.forage.entity.DetailDevis;
import com.forage.entity.Devis;
import com.forage.entity.Statut;
import com.forage.service.DemandeService;
import com.forage.service.DevisService;
import com.forage.service.ProduitService;
import com.forage.service.StatutService;
import com.forage.service.TypeDevisService;
import com.forage.util.WorkingHoursCalculator;

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

    @Autowired
    private ProduitService produitService;

    @GetMapping
    public String listDevis(Model model) {
        model.addAttribute("devis", devisService.findAll());
        return "devis/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        Devis devis = new Devis();
        devis.setDemande(new Demande());
        devis.setDetails(new ArrayList<>());
        model.addAttribute("devis", devis);
        // Only show validated demandes in dropdown
        model.addAttribute("demandes", demandeService.findValidatedDemandes());
        model.addAttribute("typesDevis", typeDevisService.findAll());
        model.addAttribute("produits", produitService.findActifs());
        return "devis/form";
    }

    @GetMapping("/new-from-demande/{demandeId}")
    public String showCreateFormFromDemande(@PathVariable Long demandeId, Model model) {
        Demande demande = demandeService.findById(demandeId).orElse(null);
        if (demande == null) {
            return "redirect:/devis";
        }
        
        Devis devis = new Devis();
        devis.setDemande(demande);
        devis.setDetails(new ArrayList<>());
        
        // Initialiser le statut du devis à "En cours"
        Statut statutEnCours = statutService.findByLibelle("En cours");
        if (statutEnCours != null) {
            devis.setStatut(statutEnCours);
        }
        
        model.addAttribute("devis", devis);
        model.addAttribute("typesDevis", typeDevisService.findAll());
        model.addAttribute("produits", produitService.findActifs());
        return "devis/form-from-demande";
    }

    @PostMapping
    public String saveDevis(@ModelAttribute Devis devis) {
        if (devis.getDemande() != null && devis.getDemande().getId() != null) {
            demandeService.findById(devis.getDemande().getId()).ifPresent(devis::setDemande);
        }
        // Initialiser le statut si non défini
        if (devis.getStatut() == null) {
            Statut statutEnCours = statutService.findByLibelle("En cours");
            if (statutEnCours != null) {
                devis.setStatut(statutEnCours);
            }
        }
        
        devisService.save(devis);
        return "redirect:/devis/" + devis.getId();
    }

    @GetMapping("/{id}")
    public String viewDevis(@PathVariable Long id, Model model) {
        Devis devis = devisService.findById(id).orElse(null);
        if (devis == null) {
            return "redirect:/devis";
        }
        model.addAttribute("devis", devis);
        model.addAttribute("detail", new DetailDevis());
        model.addAttribute("produits", produitService.findActifs());
        
        // Ajouter la date/heure actuelle
        model.addAttribute("currentDateTime", LocalDateTime.now());
        
        // Calculer la différence entre la date de demande et la date de devis
        if (devis.getDemande() != null && devis.getDemande().getDateDemande() != null && devis.getDateDevis() != null) {
            WorkingHoursCalculator.WorkingHoursDifference difference = WorkingHoursCalculator.calculateDifference(
                devis.getDemande().getDateDemande(), 
                devis.getDateDevis()
            );
            model.addAttribute("workingHoursDifference", difference);
        }
        
        return "devis/view";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Devis devis = devisService.findById(id).orElse(null);
        if (devis == null) {
            return "redirect:/devis";
        }
        model.addAttribute("devis", devis);
        // Only show validated demandes in dropdown
        model.addAttribute("demandes", demandeService.findValidatedDemandes());
        model.addAttribute("typesDevis", typeDevisService.findAll());
        model.addAttribute("produits", produitService.findActifs());
        return "devis/form";
    }

    @PostMapping("/update/{id}")
    public String updateDevis(@PathVariable Long id, @ModelAttribute Devis devis) {
        devis.setId(id);
        if (devis.getDemande() != null && devis.getDemande().getId() != null) {
            demandeService.findById(devis.getDemande().getId()).ifPresent(devis::setDemande);
        }
        devisService.save(devis);
        return "redirect:/devis/" + id;
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

    /**
     * Valider un devis - change son statut à "Terminé"
     */
    @GetMapping("/valider/{id}")
    public String validerDevis(@PathVariable Long id) {
        devisService.validerDevis(id);
        return "redirect:/devis";
    }

    /**
     * Rejeter un devis - change son statut à "Rejeté"
     */
    @GetMapping("/rejeter/{id}")
    public String rejeterDevis(@PathVariable Long id) {
        devisService.rejeterDevis(id);
        return "redirect:/devis";
    }
}
