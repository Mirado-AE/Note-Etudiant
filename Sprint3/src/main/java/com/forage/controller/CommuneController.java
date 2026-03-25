package com.forage.controller;

import com.forage.entity.Commune;
import com.forage.service.CommuneService;
import com.forage.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/communes")
public class CommuneController {

    @Autowired
    private CommuneService communeService;

    @Autowired
    private DistrictService districtService;

    @GetMapping
    public String listCommunes(Model model) {
        model.addAttribute("communes", communeService.findAll());
        return "communes/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("commune", new Commune());
        model.addAttribute("districts", districtService.findAll());
        return "communes/form";
    }

    @PostMapping
    public String saveCommune(@ModelAttribute Commune commune) {
        communeService.save(commune);
        return "redirect:/communes";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("commune", communeService.findById(id).orElse(null));
        model.addAttribute("districts", districtService.findAll());
        return "communes/form";
    }

    @PostMapping("/update/{id}")
    public String updateCommune(@PathVariable Long id, @ModelAttribute Commune commune) {
        commune.setId(id);
        communeService.save(commune);
        return "redirect:/communes";
    }

    @GetMapping("/delete/{id}")
    public String deleteCommune(@PathVariable Long id) {
        communeService.deleteById(id);
        return "redirect:/communes";
    }

    @GetMapping("/by-district/{districtId}")
    public String getByDistrict(@PathVariable Long districtId, Model model) {
        model.addAttribute("communes", communeService.findByDistrictId(districtId));
        return "communes/list";
    }
}
