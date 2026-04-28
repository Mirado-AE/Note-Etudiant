package com.forage.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "details_devis")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public class DetailDevis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_devis", nullable = false)
    private Devis devis;

    @ManyToOne
    @JoinColumn(name = "id_produit", nullable = false)
    private Produit produit;

    @Column(nullable = false)
    private Integer quantite;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal prixUnitaire;

    @Column(precision = 15, scale = 2)
    private BigDecimal remiseUnitaire = BigDecimal.ZERO;

    @Transient
    public BigDecimal getPrixApresRemise() {
        if (prixUnitaire != null && remiseUnitaire != null) {
            return prixUnitaire.subtract(remiseUnitaire);
        }
        return prixUnitaire != null ? prixUnitaire : BigDecimal.ZERO;
    }

    @Transient
    public BigDecimal getSousTotal() {
        if (prixUnitaire != null && quantite != null) {
            BigDecimal montantBrut = prixUnitaire.multiply(BigDecimal.valueOf(quantite));
            if (remiseUnitaire != null && remiseUnitaire.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal remiseTotale = remiseUnitaire.multiply(BigDecimal.valueOf(quantite));
                return montantBrut.subtract(remiseTotale);
            }
            return montantBrut;
        }
        return BigDecimal.ZERO;
    }

    public void calculerRemiseAutomatique() {
        if (prixUnitaire != null) {
            // Appliquer une remise unitaire de 10% sur tous les détails
            this.remiseUnitaire = prixUnitaire.multiply(new BigDecimal("0.10"));
        } else {
            this.remiseUnitaire = BigDecimal.ZERO;
        }
    }
}
