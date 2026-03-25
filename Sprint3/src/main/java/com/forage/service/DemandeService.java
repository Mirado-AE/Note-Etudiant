package com.forage.service;

import com.forage.entity.Demande;
import com.forage.entity.Statut;
import com.forage.repository.DemandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DemandeService {

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private StatutService statutService;

    public List<Demande> findAll() {
        return demandeRepository.findAll();
    }

    public Optional<Demande> findById(Long id) {
        return demandeRepository.findById(id);
    }

    public Demande save(Demande demande) {
        // If this is a new demande and no statut is set, create one automatically
        if (demande.getId() == null && demande.getStatut() == null) {
            // Find the "En cours" statut by default for new demandes
            List<Statut> statuts = statutService.searchByLibelle("En cours");
            if (!statuts.isEmpty()) {
                demande.setStatut(statuts.get(0));
            } else {
                // If "En cours" doesn't exist, try to get "Brouillon"
                statuts = statutService.searchByLibelle("Brouillon");
                if (!statuts.isEmpty()) {
                    demande.setStatut(statuts.get(0));
                }
            }
        }
        return demandeRepository.save(demande);
    }

    public void deleteById(Long id) {
        demandeRepository.deleteById(id);
    }

    public List<Demande> findByClientId(Long clientId) {
        return demandeRepository.findByClientId(clientId);
    }

    public List<Demande> findByRegionId(Long regionId) {
        return demandeRepository.findByRegionId(regionId);
    }

    public List<Demande> findByDistrictId(Long districtId) {
        return demandeRepository.findByDistrictId(districtId);
    }

    public List<Demande> findByCommuneId(Long communeId) {
        return demandeRepository.findByCommuneId(communeId);
    }

    /**
     * Validate a demande by changing its statut to "Valide"
     */
    public Demande validerDemande(Long id) {
        Optional<Demande> optDemande = demandeRepository.findById(id);
        if (optDemande.isPresent()) {
            Demande demande = optDemande.get();
            // Find the "Valide" statut
            List<Statut> statuts = statutService.searchByLibelle("Valide");
            if (!statuts.isEmpty()) {
                demande.setStatut(statuts.get(0));
                return demandeRepository.save(demande);
            }
        }
        return null;
    }

    /**
     * Reject a demande by changing its statut to "Annule"
     */
    public Demande rejeterDemande(Long id) {
        Optional<Demande> optDemande = demandeRepository.findById(id);
        if (optDemande.isPresent()) {
            Demande demande = optDemande.get();
            // Find the "Annule" statut
            List<Statut> statuts = statutService.searchByLibelle("Annule");
            if (!statuts.isEmpty()) {
                demande.setStatut(statuts.get(0));
                return demandeRepository.save(demande);
            }
        }
        return null;
    }
}
