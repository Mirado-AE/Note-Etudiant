package com.forage.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String telephone;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Demande> demandes;

    @PrePersist
    protected void onCreate() {
        if (dateCreation == null) {
            dateCreation = LocalDateTime.now();
        }
    }

    @Override
    public String toString() {
        return id != null ? id.toString() : "";
    }
}
