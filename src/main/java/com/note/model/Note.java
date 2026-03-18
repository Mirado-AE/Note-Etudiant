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
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "correcteur_id", nullable = false)
    private Correcteur correcteur;

    @ManyToOne
    @JoinColumn(name = "candidat_id", nullable = false)
    private Candidat candidat;

    @ManyToOne
    @JoinColumn(name = "matiere_id", nullable = false)
    private Matiere matiere;

    @ManyToOne
    @JoinColumn(name = "examen_id", nullable = false)
    private Examen examen;

    @Column(name = "note", nullable = false)
    private double note;

    public Long getId() { return id; }
    public Correcteur getCorrecteur() { return correcteur; }
    public Candidat getCandidat() { return candidat; }
    public Matiere getMatiere() { return matiere; }
    public Examen getExamen() { return examen; }
    public double getNote() { return note; }
    
    public void setId(Long id) { this.id = id; }
    public void setCorrecteur(Correcteur correcteur) { this.correcteur = correcteur; }
    public void setCandidat(Candidat candidat) { this.candidat = candidat; }
    public void setMatiere(Matiere matiere) { this.matiere = matiere; }
    public void setExamen(Examen examen) { this.examen = examen; }
    public void setNote(double note) { this.note = note; }
    
}