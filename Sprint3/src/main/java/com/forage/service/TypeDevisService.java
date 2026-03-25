package com.forage.service;

import com.forage.entity.TypeDevis;
import com.forage.repository.TypeDevisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TypeDevisService {

    @Autowired
    private TypeDevisRepository typeDevisRepository;

    public List<TypeDevis> findAll() {
        return typeDevisRepository.findAll();
    }

    public Optional<TypeDevis> findById(Long id) {
        return typeDevisRepository.findById(id);
    }

    public TypeDevis save(TypeDevis typeDevis) {
        return typeDevisRepository.save(typeDevis);
    }

    public void deleteById(Long id) {
        typeDevisRepository.deleteById(id);
    }

    public List<TypeDevis> searchByLibelle(String libelle) {
        return typeDevisRepository.findByLibelleContainingIgnoreCase(libelle);
    }
}
