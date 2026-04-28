package com.forage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandeStatistiquesDto {
    private String statut;
    private Long nombreDemandes;
}