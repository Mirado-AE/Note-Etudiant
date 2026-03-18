package com.note.model.parametre;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Operateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "operateur", nullable = false)
    private String operateur;
    
    public Long getId() { return id; }
    public String getOperateur() { return operateur; }

    public void setId(Long id) { this.id = id; }
    public void setOperateur(String operateur) { this.operateur = operateur; }
}
