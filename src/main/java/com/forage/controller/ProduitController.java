package com.forage.controller;

import com.forage.entity.Produit;
import com.forage.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/produits")
public class ProduitController {

    @Autowired
    private ProduitService produitService;

    @GetMapping
    public String listProduits(Model model) {
        model.addAttribute("produits", produitService.findAll());
        return "produits/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("produit", new Produit());
        return "produits/form";
    }

    @PostMapping
    public String saveProduit(@ModelAttribute Produit produit) {
        produitService.save(produit);
        return "redirect:/produits";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Produit produit = produitService.findById(id).orElse(null);
        if (produit == null) {
            return "redirect:/produits";
        }
        model.addAttribute("produit", produit);
        return "produits/form";
    }

    @PostMapping("/update/{id}")
    public String updateProduit(@PathVariable Long id, @ModelAttribute Produit produit) {
        produit.setId(id);
        produitService.save(produit);
        return "redirect:/produits";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduit(@PathVariable Long id) {
        produitService.deleteById(id);
        return "redirect:/produits";
    }

    @GetMapping("/actifs")
    public String listActifs(Model model) {
        model.addAttribute("produits", produitService.findActifs());
        return "produits/list";
    }
}
