package com.forage.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "demande_historique")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public class DemandeHistorique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_demande", nullable = false)
    private Demande demande;

    @Column(name = "date_action", nullable = false)
    private LocalDateTime dateAction;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "ancien_statut")
    private String ancienStatut;

    @Column(name = "nouveau_statut")
    private String nouveauStatut;

    @Column(name = "commentaire", columnDefinition = "TEXT")
    private String commentaire;

    @PrePersist
    protected void onCreate() {
        if (dateAction == null) {
            dateAction = LocalDateTime.now();
        }
    }
}
