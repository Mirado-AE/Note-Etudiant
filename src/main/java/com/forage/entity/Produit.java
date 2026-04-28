package com.forage.entity;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "produits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal prixUnitaire;

    @Column(nullable = false)
    private String unite; // ex: "unité", "heure", "kg", etc.

    @Column(nullable = false)
    private Boolean actif = true;

    @OneToMany(mappedBy = "produit")
    private List<DetailDevis> detailsDevis;

    @Override
    public String toString() {
        return nom + " (" + unite + ")";
    }
}
