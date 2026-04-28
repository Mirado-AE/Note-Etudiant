package com.forage.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.forage.repository.ClientRepository;
import com.forage.repository.DemandeRepository;
import com.forage.repository.DetailDevisRepository;
import com.forage.repository.DevisRepository;
import com.forage.repository.PaiementRepository;

@Controller
public class HomeController {

    private final ClientRepository clientRepository;
    private final DemandeRepository demandeRepository;
    private final DevisRepository devisRepository;
    private final PaiementRepository paiementRepository;
    private final DetailDevisRepository detailDevisRepository;

    public HomeController(ClientRepository clientRepository, 
                          DemandeRepository demandeRepository,
                          DevisRepository devisRepository,
                          PaiementRepository paiementRepository,
                          DetailDevisRepository detailDevisRepository) {
        this.clientRepository = clientRepository;
        this.demandeRepository = demandeRepository;
        this.devisRepository = devisRepository;
        this.paiementRepository = paiementRepository;
        this.detailDevisRepository = detailDevisRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("demandes", demandeRepository.findAll());
        model.addAttribute("devis", devisRepository.findAll());
        model.addAttribute("paiements", paiementRepository.findAll());
        
        // Chiffre d'affaire
        BigDecimal chiffreAffaire = detailDevisRepository.getChiffreAffairePrevisionnelTotal();
        model.addAttribute("chiffreAffaire", chiffreAffaire != null ? chiffreAffaire : BigDecimal.ZERO);
        
        // Statistiques séparées par type
        List<Object[]> statsDevisParStatut = detailDevisRepository.getStatistiquesDevisParStatut();
        List<Object[]> statsDemandesParStatut = demandeRepository.getStatistiquesDemandesParStatut();
        
        model.addAttribute("statsDevisParStatut", statsDevisParStatut);
        model.addAttribute("statsDemandesParStatut", statsDemandesParStatut);
        
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        return "about";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        return "contact";
    }
}
