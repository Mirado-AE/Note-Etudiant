package com.note.model.parametre;

import com.note.model.Matiere;

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
public class Parametre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "matiere_id", nullable = false)
    private Matiere matiere;

    @ManyToOne
    @JoinColumn(name = "operateur_id", nullable = false)
    private Operateur operateur;

    @Column(name = "seuil_sum_diff", nullable = false)
    private double seuilSumDiff;
    
    @ManyToOne
    @JoinColumn(name = "resolution_id", nullable = false)
    private Resolution resolution;

    public Long getId() { return id; }
    public Matiere getMatiere() { return matiere; }
    public Operateur getOperateur() { return operateur; }
    public double getSeuilSumDiff() { return seuilSumDiff; }
    public Resolution getResolution() { return resolution; }

    public void setId(Long id) { this.id = id; }
    public void setMatiere(Matiere matiere) { this.matiere = matiere; }
    public void setOperateur(Operateur operateur) { this.operateur = operateur; }
    public void setSeuilSumDiff(double seuilSumDiff) { this.seuilSumDiff = seuilSumDiff; }
    public void setResolution(Resolution resolution) { this.resolution = resolution; }
}
