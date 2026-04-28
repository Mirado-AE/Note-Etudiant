package com.forage.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "statut")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String libelle;

    @OneToMany(mappedBy = "statut")
    private List<Devis> devis;

    @OneToMany(mappedBy = "statut")
    private List<Demande> demandes;

    @Override
    public String toString() {
        return id != null ? id.toString() : "";
    }
}
