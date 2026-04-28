package com.forage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forage.entity.Commune;
import com.forage.entity.District;
import com.forage.service.ClientService;
import com.forage.service.CommuneService;
import com.forage.service.DemandeService;
import com.forage.service.DistrictService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiController {

    @Autowired
    private DistrictService districtService;

    @Autowired
    private CommuneService communeService;

    @GetMapping("/districts-by-region/{regionId}")
    public List<District> getDistrictsByRegion(@PathVariable Long regionId) {
        return districtService.findByRegionId(regionId);
    }

    @GetMapping("/communes-by-district/{districtId}")
    public List<Commune> getCommunesByDistrict(@PathVariable Long districtId) {
        return communeService.findByDistrictId(districtId);
    }

    @Autowired
    private ClientService clientService;

    @Autowired
    private DemandeService demandeService;

    @GetMapping("/client/{id}")
    public ResponseEntity<Map<String, Object>> getClientInfo(@PathVariable Long id) {
        return clientService.findById(id)
                .map(client -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("id", client.getId());
                    result.put("nom", client.getNom());
                    result.put("email", client.getEmail());
                    result.put("telephone", client.getTelephone());
                    result.put("demandesCount", client.getDemandes() != null ? client.getDemandes().size() : 0);
                    return ResponseEntity.ok(result);
                })
                .orElseGet(() -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("error", "Client introuvable pour cet ID.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @GetMapping("/demande/{id}")
    public ResponseEntity<Map<String, Object>> getDemandeInfo(@PathVariable Long id) {
        return demandeService.findById(id)
                .map(demande -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("id", demande.getId());
                    result.put("statut", demande.getStatut() != null ? demande.getStatut().getLibelle() : "Aucun statut");
                    result.put("dateDemande", demande.getDateDemande() != null ? demande.getDateDemande().toString() : null);
                    if (demande.getClient() != null) {
                        result.put("clientId", demande.getClient().getId());
                        result.put("clientNom", demande.getClient().getNom());
                        result.put("clientEmail", demande.getClient().getEmail());
                        result.put("clientTelephone", demande.getClient().getTelephone());
                    }
                    if (demande.getRegion() != null) {
                        result.put("region", demande.getRegion().getNom());
                    }
                    if (demande.getDistrict() != null) {
                        result.put("district", demande.getDistrict().getNom());
                    }
                    if (demande.getCommune() != null) {
                        result.put("commune", demande.getCommune().getNom());
                    }
                    result.put("observation", demande.getObservation());
                    return ResponseEntity.ok(result);
                })
                .orElseGet(() -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("error", "Demande introuvable pour cet ID.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }
}
