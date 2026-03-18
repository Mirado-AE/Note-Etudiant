package com.note.model;

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
public class NoteFinal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "candidat_id", nullable = false)
    private Candidat candidat;

    @ManyToOne
    @JoinColumn(name = "matiere_id", nullable = false)
    private Matiere matiere;

    @Column(name = "note", nullable = false)
    private double note;

    public Long getId() { return id; }
    public Candidat getCandidat() { return candidat; }
    public Matiere getMatiere() { return matiere; }
    public double getNote() { return note; }
    
    public void setId(Long id) { this.id = id; }
    public void setCandidat(Candidat candidat) { this.candidat = candidat; }
    public void setMatiere(Matiere matiere) { this.matiere = matiere; }
    public void setNote(double note) { this.note = note; }
    
}