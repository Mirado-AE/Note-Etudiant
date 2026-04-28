package com.forage.controller;

import com.forage.entity.Client;
import com.forage.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public String listClients(Model model) {
        model.addAttribute("clients", clientService.findAll());
        return "clients/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("client", new Client());
        return "clients/form";
    }

    @PostMapping
    public String saveClient(@ModelAttribute Client client) {
        clientService.save(client);
        return "redirect:/clients";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientService.findById(id).orElse(null));
        return "clients/form";
    }

    @PostMapping("/update/{id}")
    public String updateClient(@PathVariable Long id, @ModelAttribute Client client) {
        client.setId(id);
        clientService.save(client);
        return "redirect:/clients";
    }

    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable Long id) {
        clientService.deleteById(id);
        return "redirect:/clients";
    }

    @GetMapping("/search")
    public String searchClients(@RequestParam String nom, Model model) {
        model.addAttribute("clients", clientService.searchByNom(nom));
        return "clients/list";
    }
}
