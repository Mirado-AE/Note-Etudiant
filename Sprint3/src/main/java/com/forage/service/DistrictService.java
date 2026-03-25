package com.forage.service;

import com.forage.entity.District;
import com.forage.repository.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DistrictService {

    @Autowired
    private DistrictRepository districtRepository;

    public List<District> findAll() {
        return districtRepository.findAll();
    }

    public Optional<District> findById(Long id) {
        return districtRepository.findById(id);
    }

    public District save(District district) {
        return districtRepository.save(district);
    }

    public District update(Long id, District district) {
        District existing = districtRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("District not found with id: " + id));
        existing.setNom(district.getNom());
        existing.setRegion(district.getRegion());
        return districtRepository.save(existing);
    }

    public void deleteById(Long id) {
        districtRepository.deleteById(id);
    }

    public List<District> searchByNom(String nom) {
        return districtRepository.findByNomContainingIgnoreCase(nom);
    }

    public List<District> findByRegionId(Long regionId) {
        return districtRepository.findByRegionId(regionId);
    }
}
