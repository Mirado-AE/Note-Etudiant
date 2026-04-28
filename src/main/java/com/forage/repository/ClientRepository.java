package com.forage.repository;

import com.forage.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    List<Client> findByNomContainingIgnoreCase(String nom);
    
    List<Client> findByTelephoneContaining(String telephone);
}
