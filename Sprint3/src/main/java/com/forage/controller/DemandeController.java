package com.forage.controller;

import com.forage.entity.Demande;
import com.forage.service.ClientService;
import com.forage.service.DemandeService;
import com.forage.service.RegionService;
import com.forage.service.DistrictService;
import com.forage.service.CommuneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/demandes")
public class DemandeController {

    @Autowired
    private DemandeService demandeService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private CommuneService communeService;

    @GetMapping
    public String listDemandes(Model model) {
        model.addAttribute("demandes", demandeService.findAll());
        return "demandes/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("demande", new Demande());
        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("regions", regionService.findAll());
        model.addAttribute("districts", districtService.findAll());
        model.addAttribute("communes", communeService.findAll());
        return "demandes/form";
    }

    @PostMapping
    public String saveDemande(@ModelAttribute Demande demande) {
        demandeService.save(demande);
        return "redirect:/demandes";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("demande", demandeService.findById(id).orElse(null));
        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("regions", regionService.findAll());
        model.addAttribute("districts", districtService.findAll());
        model.addAttribute("communes", communeService.findAll());
        return "demandes/form";
    }

    @PostMapping("/update/{id}")
    public String updateDemande(@PathVariable Long id, @ModelAttribute Demande demande) {
        demande.setId(id);
        demandeService.save(demande);
        return "redirect:/demandes";
    }

    @GetMapping("/delete/{id}")
    public String deleteDemande(@PathVariable Long id) {
        demandeService.deleteById(id);
        return "redirect:/demandes";
    }

    @GetMapping("/by-client/{clientId}")
    public String getByClient(@PathVariable Long clientId, Model model) {
        model.addAttribute("demandes", demandeService.findByClientId(clientId));
        return "demandes/list";
    }

    @GetMapping("/by-region/{regionId}")
    public String getByRegion(@PathVariable Long regionId, Model model) {
        model.addAttribute("demandes", demandeService.findByRegionId(regionId));
        return "demandes/list";
    }

    /**
     * Valider une demande - change son statut à "Valide"
     */
    @GetMapping("/valider/{id}")
    public String validerDemande(@PathVariable Long id) {
        demandeService.validerDemande(id);
        return "redirect:/demandes";
    }

    /**
     * Rejeter une demande - change son statut à "Annule"
     */
    @GetMapping("/rejeter/{id}")
    public String rejeterDemande(@PathVariable Long id) {
        demandeService.rejeterDemande(id);
        return "redirect:/demandes";
    }
}
