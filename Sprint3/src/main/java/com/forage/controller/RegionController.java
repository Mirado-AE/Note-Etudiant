package com.forage.controller;

import com.forage.entity.Region;
import com.forage.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/regions")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @GetMapping
    public String listRegions(Model model) {
        model.addAttribute("regions", regionService.findAll());
        return "regions/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("region", new Region());
        return "regions/form";
    }

    @PostMapping
    public String saveRegion(@ModelAttribute Region region) {
        regionService.save(region);
        return "redirect:/regions";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("region", regionService.findById(id).orElse(null));
        return "regions/form";
    }

    @PostMapping("/update/{id}")
    public String updateRegion(@PathVariable Long id, @ModelAttribute Region region) {
        region.setId(id);
        regionService.save(region);
        return "redirect:/regions";
    }

    @GetMapping("/delete/{id}")
    public String deleteRegion(@PathVariable Long id) {
        regionService.deleteById(id);
        return "redirect:/regions";
    }

    @GetMapping("/search")
    public String searchRegions(@RequestParam String nom, Model model) {
        model.addAttribute("regions", regionService.searchByNom(nom));
        return "regions/list";
    }
}
