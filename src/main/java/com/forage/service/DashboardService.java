package com.forage.service;

import com.forage.dto.ChiffreAffaireDto;
import com.forage.dto.StatutStatistiquesDto;
import com.forage.repository.DetailDevisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    @Autowired
    private DetailDevisRepository detailDevisRepository;

    public BigDecimal getChiffreAffairePrevisionnelTotal() {
        BigDecimal total = detailDevisRepository.getChiffreAffairePrevisionnelTotal();
        return total != null ? total : BigDecimal.ZERO;
    }

    public List<ChiffreAffaireDto> getChiffreAffaireParDevis() {
        List<Object[]> results = detailDevisRepository.getChiffreAffaireParDevis();
        List<ChiffreAffaireDto> dtos = new ArrayList<>();

        for (Object[] row : results) {
            ChiffreAffaireDto dto = new ChiffreAffaireDto();
            dto.setDevisId(((Number) row[0]).longValue());
            dto.setDateDevis((LocalDateTime) row[1]);
            dto.setDemandeId(((Number) row[2]).longValue());
            dto.setClientNom((String) row[3]);
            dto.setTypeDevis((String) row[4]);
            dto.setStatut((String) row[5]);
            dto.setMontantTotal((BigDecimal) row[6]);
            dtos.add(dto);
        }

        return dtos;
    }

    public List<StatutStatistiquesDto> getStatistiquesParStatut() {
        List<Object[]> results = detailDevisRepository.getStatistiquesParStatut();
        List<StatutStatistiquesDto> dtos = new ArrayList<>();

        for (Object[] row : results) {
            StatutStatistiquesDto dto = new StatutStatistiquesDto();
            dto.setStatut((String) row[0]);
            dto.setNombreDevis(((Number) row[1]).longValue());
            dto.setMontantTotal((BigDecimal) row[2]);
            dtos.add(dto);
        }

        return dtos;
    }
}
