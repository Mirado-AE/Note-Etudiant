package com.forage.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.forage.dto.ChiffreAffaireDto;
import com.forage.dto.StatutStatistiquesDto;
import com.forage.service.DashboardService;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public String showDashboard(Model model) {
        // Récupérer le chiffre d'affaire total prévisionnel
        BigDecimal totalChiffreAffaire = dashboardService.getChiffreAffairePrevisionnelTotal();
        
        // Récupérer le détail par devis
        List<ChiffreAffaireDto> chiffreAffaireParDevis = dashboardService.getChiffreAffaireParDevis();

        // Récupérer les statistiques par statut
        List<StatutStatistiquesDto> statistiquesParStatut = dashboardService.getStatistiquesParStatut();

        model.addAttribute("totalChiffreAffaire", totalChiffreAffaire);
        model.addAttribute("chiffreAffaireParDevis", chiffreAffaireParDevis);
        model.addAttribute("statistiquesParStatut", statistiquesParStatut);

        return "dashboard/index";
    }
}
