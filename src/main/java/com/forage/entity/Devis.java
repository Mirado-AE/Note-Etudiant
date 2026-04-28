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
@SuppressWarnings("unused")
public class Devis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_devis")
    private LocalDateTime dateDevis;

    @ManyToOne
    @JoinColumn(name = "id_demande", nullable = false)
    private Demande demande;

    @ManyToOne
    @JoinColumn(name = "id_type_devis", nullable = false)
    private TypeDevis typeDevis;

    @ManyToOne
    @JoinColumn(name = "id_statut", nullable = false)
    private Statut statut;

    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailDevis> details;

    @OneToMany(mappedBy = "devis", cascade = CascadeType.ALL)
    private List<Paiement> paiements;

    @Transient
    public BigDecimal getMontantTotal() {
        if (details == null || details.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return details.stream()
                .map(DetailDevis::getSousTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @PrePersist
    protected void onCreate() {
        if (dateDevis == null) {
            dateDevis = LocalDateTime.now();
        }
    }

    @Override
    public String toString() {
        return id != null ? id.toString() : "";
    }
}
