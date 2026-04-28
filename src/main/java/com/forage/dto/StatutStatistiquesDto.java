package com.forage.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatutStatistiquesDto {
    private String statut;
    private Long nombreDevis;
    private BigDecimal montantTotal;
}