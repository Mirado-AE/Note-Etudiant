package com.forage.controller;

import com.forage.repository.ClientRepository;
import com.forage.repository.DemandeRepository;
import com.forage.repository.DevisRepository;
import com.forage.repository.PaiementRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ClientRepository clientRepository;
    private final DemandeRepository demandeRepository;
    private final DevisRepository devisRepository;
    private final PaiementRepository paiementRepository;

    public HomeController(ClientRepository clientRepository, 
                          DemandeRepository demandeRepository,
                          DevisRepository devisRepository,
                          PaiementRepository paiementRepository) {
        this.clientRepository = clientRepository;
        this.demandeRepository = demandeRepository;
        this.devisRepository = devisRepository;
        this.paiementRepository = paiementRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("demandes", demandeRepository.findAll());
        model.addAttribute("devis", devisRepository.findAll());
        model.addAttribute("paiements", paiementRepository.findAll());
        return "index";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "dashboard";
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
