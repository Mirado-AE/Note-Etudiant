package com.forage.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "type_devis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeDevis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String libelle;

    @OneToMany(mappedBy = "typeDevis")
    private List<Devis> devis;

    @Override
    public String toString() {
        return id != null ? id.toString() : "";
    }
}
