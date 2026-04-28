package com.forage.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.forage.entity.Demande;
import com.forage.entity.DemandeHistorique;
import com.forage.entity.Statut;
import com.forage.repository.DemandeHistoriqueRepository;

@Service
@Transactional
public class DemandeHistoriqueService {

    @Autowired
    private DemandeHistoriqueRepository demandeHistoriqueRepository;

    public List<DemandeHistorique> findByDemandeId(Long demandeId) {
        return demandeHistoriqueRepository.findByDemandeIdOrderByDateActionDesc(demandeId);
    }

    public DemandeHistorique recordCreation(Demande demande) {
        DemandeHistorique historique = new DemandeHistorique();
        historique.setDemande(demande);
        historique.setAction("Création de la demande");
        historique.setNouveauStatut(demande.getStatut() != null ? demande.getStatut().getLibelle() : null);
        historique.setCommentaire("Demande créée avec le statut initial.");
        return demandeHistoriqueRepository.save(historique);
    }

    public DemandeHistorique recordUpdate(Demande ancien, Demande actuel) {
        if (ancien == null || actuel == null) {
            return null;
        }

        String ancienStatut = ancien.getStatut() != null ? ancien.getStatut().getLibelle() : null;
        String nouveauStatut = actuel.getStatut() != null ? actuel.getStatut().getLibelle() : null;
        StringBuilder commentaire = new StringBuilder();

        if (!Objects.equals(ancien.getRegion(), actuel.getRegion())) {
            commentaire.append("Région modifiée. ");
        }
        if (!Objects.equals(ancien.getDistrict(), actuel.getDistrict())) {
            commentaire.append("District modifié. ");
        }
        if (!Objects.equals(ancien.getCommune(), actuel.getCommune())) {
            commentaire.append("Commune modifiée. ");
        }
        if (!Objects.equals(ancien.getObservation(), actuel.getObservation())) {
            commentaire.append("Observation mise à jour. ");
        }
        if (!Objects.equals(ancien.getDescription(), actuel.getDescription())) {
            commentaire.append("Description mise à jour. ");
        }
        if (!Objects.equals(ancienStatut, nouveauStatut)) {
            commentaire.append("Statut changé de ").append(ancienStatut != null ? ancienStatut : "aucun").append(" à ").append(nouveauStatut).append(". ");
        }

        if (commentaire.isEmpty()) {
            commentaire.append("Modification enregistrée.");
        }

        DemandeHistorique historique = new DemandeHistorique();
        historique.setDemande(actuel);
        historique.setAction("Mise à jour de la demande");
        historique.setAncienStatut(ancienStatut);
        historique.setNouveauStatut(nouveauStatut);
        historique.setCommentaire(commentaire.toString().trim());
        return demandeHistoriqueRepository.save(historique);
    }

    public DemandeHistorique recordStatusChange(Demande demande, Statut ancienStatut, Statut nouveauStatut) {
        if (demande == null) {
            return null;
        }
        String ancien = ancienStatut != null ? ancienStatut.getLibelle() : null;
        String nouveau = nouveauStatut != null ? nouveauStatut.getLibelle() : null;

        DemandeHistorique historique = new DemandeHistorique();
        historique.setDemande(demande);
        historique.setAction("Changement de statut");
        historique.setAncienStatut(ancien);
        historique.setNouveauStatut(nouveau);
        historique.setCommentaire("Statut mis à jour.");
        return demandeHistoriqueRepository.save(historique);
    }
}
