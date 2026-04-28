package com.forage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.forage.entity.Client;
import com.forage.entity.Demande;
import com.forage.entity.Statut;
import com.forage.service.ClientService;
import com.forage.service.CommuneService;
import com.forage.service.DemandeService;
import com.forage.service.DistrictService;
import com.forage.service.RegionService;
import com.forage.service.StatutService;

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

    @Autowired
    private StatutService statutService;

    @Autowired
    private com.forage.service.DemandeHistoriqueService demandeHistoriqueService;

    @GetMapping
    public String listDemandes(Model model) {
        model.addAttribute("demandes", demandeService.findAll());
        return "demandes/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        Demande demande = new Demande();
        demande.setClient(new Client());
        model.addAttribute("demande", demande);
        model.addAttribute("regions", regionService.findAll());
        model.addAttribute("districts", districtService.findAll());
        model.addAttribute("communes", communeService.findAll());
        return "demandes/form";
    }

    @PostMapping
    public String saveDemande(@ModelAttribute Demande demande) {
        if (demande.getClient() != null && demande.getClient().getId() != null) {
            clientService.findById(demande.getClient().getId()).ifPresent(demande::setClient);
        }
        // Initialiser le statut à "Créée" si ce n'est pas défini
        if (demande.getStatut() == null) {
            Statut statutCreee = statutService.findByLibelle("Créée");
            if (statutCreee != null) {
                demande.setStatut(statutCreee);
            }
        }
        demandeService.save(demande);
        return "redirect:/demandes";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Demande demande = demandeService.findById(id).orElse(null);
        if (demande == null) {
            return "redirect:/demandes";
        }
        if (demande.getClient() == null) {
            demande.setClient(new Client());
        }
        model.addAttribute("demande", demande);
        model.addAttribute("regions", regionService.findAll());
        model.addAttribute("districts", districtService.findAll());
        model.addAttribute("communes", communeService.findAll());
        model.addAttribute("historique", demandeHistoriqueService.findByDemandeId(id));
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
     * Valider une demande - change son statut à "Validée"
     */
    @GetMapping("/valider/{id}")
    public String validerDemande(@PathVariable Long id) {
        demandeService.validerDemande(id);
        return "redirect:/demandes";
    }

    /**
     * Rejeter une demande - change son statut à "Rejetée"
     */
    @GetMapping("/rejeter/{id}")
    public String rejeterDemande(@PathVariable Long id) {
        demandeService.rejeterDemande(id);
        return "redirect:/demandes";
    }

    /**
     * Changer le statut d'une demande
     */
    @GetMapping("/changer-statut/{id}/{statutId}")
    public String changerStatut(@PathVariable Long id, @PathVariable Long statutId) {
        demandeService.changerStatut(id, statutId);
        return "redirect:/demandes";
    }
}
