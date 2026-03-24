package com.forage.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "devis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Devis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "montant_total", precision = 15, scale = 2)
    private BigDecimal montantTotal;

    @Column(name = "date_devis")
    private LocalDateTime dateDevis;

    @Column(name = "statut_paiement")
    private String statutPaiement;

    @ManyToOne
    @JoinColumn(name = "id_demande", nullable = false)
    private Demande demande;

    @ManyToOne
    @JoinColumn(name = "id_type_devis", nullable = false)
    private TypeDevis typeDevis;

    @ManyToOne
    @JoinColumn(name = "id_statut", nullable = false)
    private Statut statut;

    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL)
    private List<DetailDevis> details;

    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL)
    private List<Paiement> paiements;

    @PrePersist
    protected void onCreate() {
        if (dateDevis == null) {
            dateDevis = LocalDateTime.now();
        }
    }
}
