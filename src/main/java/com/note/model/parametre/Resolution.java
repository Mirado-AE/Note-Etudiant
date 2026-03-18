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
public class Resolution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "resolution", nullable = false)
    private String resolution;
    
    public Long getId() { return id; }
    public String getResolution() { return resolution; }

    public void setId(Long id) { this.id = id; }
    public void setResolution(String resolution) { this.resolution = resolution; }
}
