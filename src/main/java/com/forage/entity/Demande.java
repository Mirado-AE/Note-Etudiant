package com.forage.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "demandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_demande")
    private LocalDateTime dateDemande;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "id_region", nullable = false)
    private Region region;

    @ManyToOne
    @JoinColumn(name = "id_district", nullable = false)
    private District district;

    @ManyToOne
    @JoinColumn(name = "id_commune", nullable = false)
    private Commune commune;

    @ManyToOne
    @JoinColumn(name = "id_statut")
    private Statut statut;

    @Column(name = "description")
    private String description;

    @Column(name = "observation")
    private String observation;

    @Column(name = "version", nullable = false)
    private Integer version = 1;

    @OneToMany(mappedBy = "demande", cascade = CascadeType.ALL)
    private List<Devis> devis;

    @PrePersist
    protected void onCreate() {
        if (dateDemande == null) {
            dateDemande = LocalDateTime.now();
        }
        if (version == null) {
            version = 1;
        }
    }

    @Override
    public String toString() {
        return id != null ? id.toString() : "";
    }
}
