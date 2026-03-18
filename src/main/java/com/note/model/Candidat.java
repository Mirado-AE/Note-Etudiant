package com.note.model;

import java.util.List;

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
public class Candidat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nom", nullable = false)
    private String nom;
    
    @Column(name = "prenom", nullable = false)
    private String prenom;
    
    @Column(name = "adresse")
    private String adresse;
    
    @Column(name = "lien_photo")
    private String lienPhoto;

    @OneToMany(mappedBy = "candidat")
    private List<Note> notes;

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getAdresse() { return adresse; }
    public String getLienPhoto() { return lienPhoto; }
    public List<Note> getNotes() { return notes; }

    public void setId(Long id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setLienPhoto(String lienPhoto) { this.lienPhoto = lienPhoto; }
    public void setNotes(List<Note> notes) { this.notes = notes; }
    
}