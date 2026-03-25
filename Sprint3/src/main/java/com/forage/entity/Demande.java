package com.forage.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "demandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToMany(mappedBy = "demande", cascade = CascadeType.ALL)
    private List<Devis> devis;

    @PrePersist
    protected void onCreate() {
        if (dateDemande == null) {
            dateDemande = LocalDateTime.now();
        }
    }

    @Override
    public String toString() {
        return id != null ? id.toString() : "";
    }
}
