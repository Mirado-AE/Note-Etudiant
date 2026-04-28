package com.forage.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "commune")
@Data
@NoArgsConstructor
@AllArgsConstructor@SuppressWarnings("unused")public class Commune {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @ManyToOne
    @JoinColumn(name = "id_district", nullable = false)
    @JsonIgnore
    private District district;
}
