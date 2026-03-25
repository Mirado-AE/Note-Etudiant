package com.forage.controller;

import com.forage.entity.District;
import com.forage.service.DistrictService;
import com.forage.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/districts")
public class DistrictController {

    @Autowired
    private DistrictService districtService;

    @Autowired
    private RegionService regionService;

    @GetMapping
    public String listDistricts(Model model) {
        model.addAttribute("districts", districtService.findAll());
        return "districts/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("district", new District());
        model.addAttribute("regions", regionService.findAll());
        return "districts/form";
    }

    @PostMapping
    public String saveDistrict(@ModelAttribute District district) {
        districtService.save(district);
        return "redirect:/districts";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("district", districtService.findById(id).orElse(null));
        model.addAttribute("regions", regionService.findAll());
        return "districts/form";
    }

    @PostMapping("/update/{id}")
    public String updateDistrict(@PathVariable Long id, @ModelAttribute District district) {
        districtService.update(id, district);
        return "redirect:/districts";
    }

    @GetMapping("/delete/{id}")
    public String deleteDistrict(@PathVariable Long id) {
        districtService.deleteById(id);
        return "redirect:/districts";
    }

    @GetMapping("/by-region/{regionId}")
    public String getByRegion(@PathVariable Long regionId, Model model) {
        model.addAttribute("districts", districtService.findByRegionId(regionId));
        return "districts/list";
    }
}
