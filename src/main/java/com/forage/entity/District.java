package com.forage.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "district")
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @ManyToOne
    @JoinColumn(name = "id_region", nullable = false)
    private Region region;

    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL)
    private List<Commune> communes;

    public District() {}

    public District(Long id, String nom, Region region) {
        this.id = id;
        this.nom = nom;
        this.region = region;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Region getRegion() { return region; }
    public void setRegion(Region region) { this.region = region; }

    public List<Commune> getCommunes() { return communes; }
    public void setCommunes(List<Commune> communes) { this.communes = communes; }
}
