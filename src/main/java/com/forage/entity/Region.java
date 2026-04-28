package com.forage.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "region")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<District> districts;
}
