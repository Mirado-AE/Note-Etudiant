package com.forage.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "paiements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;

    @Column(name = "date_paiement")
    private LocalDateTime datePaiement;

    @Column(name = "mode_paiement")
    private String modePaiement;

    private String reference;

    @ManyToOne
    @JoinColumn(name = "id_devis", nullable = false)
    private Devis devis;

    @PrePersist
    protected void onCreate() {
        if (datePaiement == null) {
            datePaiement = LocalDateTime.now();
        }
    }
}
