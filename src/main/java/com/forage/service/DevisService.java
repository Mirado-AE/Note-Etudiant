package com.forage.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forage.entity.Devis;
import com.forage.entity.Statut;
import com.forage.repository.DevisRepository;

@Service
@Transactional
public class DevisService {

    @Autowired
    private DevisRepository devisRepository;

    @Autowired
    private StatutService statutService;

    @Autowired
    private DemandeService demandeService;

    @Autowired
    private TypeDevisService typeDevisService;

    public List<Devis> findAll() {
        return devisRepository.findAll();
    }

    public Optional<Devis> findById(Long id) {
        return devisRepository.findById(id);
    }

    public Devis save(Devis devis) {
        if (devis.getDemande() != null && devis.getDemande().getId() != null) {
            demandeService.findById(devis.getDemande().getId()).ifPresent(devis::setDemande);
        }
        if (devis.getTypeDevis() != null && devis.getTypeDevis().getId() != null) {
            typeDevisService.findById(devis.getTypeDevis().getId()).ifPresent(devis::setTypeDevis);
        }
        if (devis.getStatut() != null && devis.getStatut().getId() != null) {
            statutService.findById(devis.getStatut().getId()).ifPresent(devis::setStatut);
        } else if (devis.getId() != null) {
            devisRepository.findById(devis.getId()).ifPresent(existing -> devis.setStatut(existing.getStatut()));
        }
        // If this is a new devis and no statut is set, set to "En cours"
        if (devis.getId() == null && (devis.getStatut() == null || devis.getStatut().getId() == null)) {
            Statut statutEnCours = statutService.findByLibelle("En cours");
            if (statutEnCours != null) {
                devis.setStatut(statutEnCours);
            }
        }
        return devisRepository.save(devis);
    }

    public void deleteById(Long id) {
        devisRepository.deleteById(id);
    }

    public List<Devis> findByDemandeId(Long demandeId) {
        return devisRepository.findByDemandeId(demandeId);
    }

    public List<Devis> findByTypeDevisId(Long typeDevisId) {
        return devisRepository.findByTypeDevisId(typeDevisId);
    }

    public List<Devis> findByStatutId(Long statutId) {
        return devisRepository.findByStatutId(statutId);
    }

    public BigDecimal calculerMontantTotal(Long devisId) {
        Optional<Devis> devis = devisRepository.findById(devisId);
        if (devis.isPresent()) {
            return devis.get().getMontantTotal();
        }
        return BigDecimal.ZERO;
    }

    /**
     * Validate a devis by changing its statut to "Accepté"
     */
    public Devis validerDevis(Long id) {
        Optional<Devis> optDevis = devisRepository.findById(id);
        if (optDevis.isPresent()) {
            Devis devis = optDevis.get();
            Statut statutAccepte = statutService.findByLibelle("Accepté");
            if (statutAccepte != null) {
                devis.setStatut(statutAccepte);
                return devisRepository.save(devis);
            }
        }
        return null;
    }

    /**
     * Reject a devis by changing its statut to "Annulé"
     */
    public Devis rejeterDevis(Long id) {
        Optional<Devis> optDevis = devisRepository.findById(id);
        if (optDevis.isPresent()) {
            Devis devis = optDevis.get();
            Statut statutAnnule = statutService.findByLibelle("Annulé");
            if (statutAnnule != null) {
                devis.setStatut(statutAnnule);
                return devisRepository.save(devis);
            }
        }
        return null;
    }

    public Devis changerStatut(Long id, Long statutId) {
        Optional<Devis> optDevis = devisRepository.findById(id);

        if (optDevis.isPresent()) {
            Optional<Statut> optStatut = statutService.findById(statutId);

            if (optStatut.isPresent()) {
                Devis devis = optDevis.get();
                devis.setStatut(optStatut.get());
                return devisRepository.save(devis);
            }
        }
        return null;
    }

    public Devis changerStatut(Long id, Statut statut) {
        Optional<Devis> optDevis = devisRepository.findById(id);

        if (optDevis.isPresent()) {
            Devis devis = optDevis.get();
            devis.setStatut(statut);
            return devisRepository.save(devis);
        }
        return null;
    }

    /**
     * Find all completed devis (for paiement dropdown)
     */
    public List<Devis> findCompletedDevis() {
        Statut statutValide = statutService.findByLibelle("Accepté");
        if (statutValide != null) {
            return devisRepository.findByStatutId(statutValide.getId());
        }
        return List.of();
    }
}
