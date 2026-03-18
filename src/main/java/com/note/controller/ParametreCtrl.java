package com.note.controller;

import com.note.model.Matiere;
import com.note.model.parametre.Operateur;
import com.note.model.parametre.Parametre;
import com.note.model.parametre.Resolution;
import com.note.service.MatiereServ;
import com.note.service.parametre.OperateurServ;
import com.note.service.parametre.ParametreServ;
import com.note.service.parametre.ResolutionServ;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/parametre")
public class ParametreCtrl {

    @Autowired
    private ParametreServ parametreServ;

    @Autowired
    private MatiereServ matiereServ;

    @Autowired
    private OperateurServ operateurServ;

    @Autowired
    private ResolutionServ resolutionServ;

    @GetMapping
    public String pageParametre(Model model) {
        List<Parametre> parametres = parametreServ.findAll();
        List<Matiere> matieres = matiereServ.findAll();
        List<Operateur> operateurs = operateurServ.findAll();
        List<Resolution> resolutions = resolutionServ.findAll();
        
        model.addAttribute("parametres", parametres);
        model.addAttribute("matieres", matieres);
        model.addAttribute("operateurs", operateurs);
        model.addAttribute("resolutions", resolutions);
        
        if (!model.containsAttribute("parametre")) {
            model.addAttribute("parametre", new Parametre());
        }
        
        model.addAttribute("activePage", "parametre");
        return "parametre/parametre";
    }

    @PostMapping("/save")
    public String saveParametre(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam("matiereId") Long matiereId,
            @RequestParam("operateurId") Long operateurId,
            @RequestParam("resolutionId") Long resolutionId,
            @RequestParam("seuilSumDiff") Double seuilSumDiff,
            RedirectAttributes redirectAttributes) {
        
        try {
            Optional<Matiere> matiereOpt = matiereServ.findById(matiereId);
            Optional<Operateur> operateurOpt = operateurServ.findById(operateurId);
            Optional<Resolution> resolutionOpt = resolutionServ.findById(resolutionId);
            
            if (!matiereOpt.isPresent() || !operateurOpt.isPresent() || !resolutionOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Une ou plusieurs références sont invalides");
                return "redirect:/parametre";
            }
            
            Parametre parametre = new Parametre();
            if (id != null && id > 0) {
                Optional<Parametre> existingParametre = parametreServ.findById(id);
                if (existingParametre.isPresent()) {
                    parametre = existingParametre.get();
                }
            }
            
            parametre.setMatiere(matiereOpt.get());
            parametre.setOperateur(operateurOpt.get());
            parametre.setResolution(resolutionOpt.get());
            parametre.setSeuilSumDiff(seuilSumDiff);
            
            if (id != null && id > 0) {
                parametre.setId(id);
                Parametre updated = parametreServ.update(id, parametre);
                if (updated != null) {
                    redirectAttributes.addFlashAttribute("success", "Paramètre modifié avec succès");
                } else {
                    redirectAttributes.addFlashAttribute("error", "Paramètre non trouvé");
                }
            } else {
                parametreServ.save(parametre);
                redirectAttributes.addFlashAttribute("success", "Paramètre ajouté avec succès");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'enregistrement : " + e.getMessage());
        }
        return "redirect:/parametre";
    }

    @GetMapping("/edit/{id}")
    public String editParametre(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Parametre> parametreOpt = parametreServ.findById(id);
        if (parametreOpt.isPresent()) {
            List<Parametre> parametres = parametreServ.findAll();
            List<Matiere> matieres = matiereServ.findAll();
            List<Operateur> operateurs = operateurServ.findAll();
            List<Resolution> resolutions = resolutionServ.findAll();
            
            model.addAttribute("parametres", parametres);
            model.addAttribute("matieres", matieres);
            model.addAttribute("operateurs", operateurs);
            model.addAttribute("resolutions", resolutions);
            model.addAttribute("parametre", parametreOpt.get());
            model.addAttribute("activePage", "parametre");
            return "parametre/parametre";
        } else {
            redirectAttributes.addFlashAttribute("error", "Paramètre non trouvé");
            return "redirect:/parametre";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteParametre(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Parametre> parametreOpt = parametreServ.findById(id);
            if (parametreOpt.isPresent()) {
                parametreServ.delete(id);
                redirectAttributes.addFlashAttribute("success", "Paramètre supprimé avec succès");
            } else {
                redirectAttributes.addFlashAttribute("error", "Paramètre non trouvé");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression");
        }
        return "redirect:/parametre";
    }

    @GetMapping("/cancel")
    public String cancelEdit() {
        return "redirect:/parametre";
    }
}