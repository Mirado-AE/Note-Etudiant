package com.forage.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forage.entity.Demande;
import com.forage.entity.Statut;
import com.forage.repository.DemandeRepository;

@Service
@Transactional
public class DemandeService {

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private StatutService statutService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private CommuneService communeService;

    @Autowired
    private DemandeHistoriqueService demandeHistoriqueService;

    public List<Demande> findAll() {
        return demandeRepository.findAll();
    }

    public Optional<Demande> findById(Long id) {
        return demandeRepository.findById(id);
    }

    public Demande save(Demande demande) {
        if (demande.getClient() != null && demande.getClient().getId() != null) {
            clientService.findById(demande.getClient().getId()).ifPresent(demande::setClient);
        }
        if (demande.getRegion() != null && demande.getRegion().getId() != null) {
            regionService.findById(demande.getRegion().getId()).ifPresent(demande::setRegion);
        }
        if (demande.getDistrict() != null && demande.getDistrict().getId() != null) {
            districtService.findById(demande.getDistrict().getId()).ifPresent(demande::setDistrict);
        }
        if (demande.getCommune() != null && demande.getCommune().getId() != null) {
            communeService.findById(demande.getCommune().getId()).ifPresent(demande::setCommune);
        }
        if (demande.getStatut() != null && demande.getStatut().getId() != null) {
            statutService.findById(demande.getStatut().getId()).ifPresent(demande::setStatut);
        } else if (demande.getId() != null) {
            demandeRepository.findById(demande.getId()).ifPresent(existing -> demande.setStatut(existing.getStatut()));
        }
        // If this is a new demande and no statut is set, set to "Créée"
        boolean isNew = demande.getId() == null;
        Demande existingDemande = null;
        if (!isNew) {
            existingDemande = demandeRepository.findById(demande.getId()).orElse(null);
        }
        if (isNew && (demande.getStatut() == null || demande.getStatut().getId() == null)) {
            Statut statutCreee = statutService.findByLibelle("Créée");
            if (statutCreee != null) {
                demande.setStatut(statutCreee);
            }
        }
        Demande saved = demandeRepository.save(demande);
        if (isNew) {
            demandeHistoriqueService.recordCreation(saved);
        } else if (existingDemande != null) {
            demandeHistoriqueService.recordUpdate(existingDemande, saved);
        }
        return saved;
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
     * Validate a demande by changing its statut to "Validée"
     */
    public Demande validerDemande(Long id) {
        Optional<Demande> optDemande = demandeRepository.findById(id);
        if (optDemande.isPresent()) {
            Demande demande = optDemande.get();
            Statut ancienStatut = demande.getStatut();
            Statut statutValidee = statutService.findByLibelle("Validée");
            if (statutValidee != null) {
                demande.setStatut(statutValidee);
                Demande saved = demandeRepository.save(demande);
                demandeHistoriqueService.recordStatusChange(saved, ancienStatut, statutValidee);
                return saved;
            }
        }
        return null;
    }

    /**
     * Reject a demande by changing its statut to "Rejetée"
     */
    public Demande rejeterDemande(Long id) {
        Optional<Demande> optDemande = demandeRepository.findById(id);
        if (optDemande.isPresent()) {
            Demande demande = optDemande.get();
            Statut ancienStatut = demande.getStatut();
            Statut statutRejetee = statutService.findByLibelle("Rejetée");
            if (statutRejetee != null) {
                demande.setStatut(statutRejetee);
                Demande saved = demandeRepository.save(demande);
                demandeHistoriqueService.recordStatusChange(saved, ancienStatut, statutRejetee);
                return saved;
            }
        }
        return null;
    }

    /**
     * Change the status of a demande to a specific status
     */
    public Demande changerStatut(Long id, Long statutId) {
        Optional<Demande> optDemande = demandeRepository.findById(id);
        if (optDemande.isPresent()) {
            Demande demande = optDemande.get();
            Statut ancienStatut = demande.getStatut();
            Optional<Statut> optStatut = statutService.findById(statutId);
            if (optStatut.isPresent()) {
                Statut nouveauStatut = optStatut.get();
                demande.setStatut(nouveauStatut);
                Demande saved = demandeRepository.save(demande);
                demandeHistoriqueService.recordStatusChange(saved, ancienStatut, nouveauStatut);
                return saved;
            }
        }
        return null;
    }

    /**
     * Find demandes by status
     */
    public List<Demande> findByStatutId(Long statutId) {
        return demandeRepository.findByStatutId(statutId);
    }

    /**
     * Find all validated demandes (for devis dropdown)
     */
    public List<Demande> findValidatedDemandes() {
        Statut statutValidee = statutService.findByLibelle("Validée");
        if (statutValidee != null) {
            return demandeRepository.findByStatutId(statutValidee.getId());
        }
        return List.of();
    }
}
