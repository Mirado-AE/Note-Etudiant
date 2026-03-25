package com.forage.service;

import com.forage.entity.Client;
import com.forage.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }

    public List<Client> searchByNom(String nom) {
        return clientRepository.findByNomContainingIgnoreCase(nom);
    }

    public List<Client> searchByContact(String contact) {
        return clientRepository.findByContactContaining(contact);
    }
}
