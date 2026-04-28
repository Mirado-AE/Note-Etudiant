package com.forage.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiffreAffaireDto {
    private Long devisId;
    private LocalDateTime dateDevis;
    private Long demandeId;
    private String clientNom;
    private String typeDevis;
    private String statut;
    private BigDecimal montantTotal;
}
