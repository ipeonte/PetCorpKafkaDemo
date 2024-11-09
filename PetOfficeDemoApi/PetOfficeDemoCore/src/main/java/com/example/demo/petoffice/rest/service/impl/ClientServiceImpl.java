package com.example.demo.petoffice.rest.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.petcorp.shared.dto.PetAdoptionDto;
import com.example.demo.petoffice.rest.dto.ClientDto;
import com.example.demo.petoffice.rest.error.PetOfficeErrors;
import com.example.demo.petoffice.rest.error.PetOfficeExeption;
import com.example.demo.petoffice.rest.jpa.model.Client;
import com.example.demo.petoffice.rest.jpa.model.ClientPetRef;
import com.example.demo.petoffice.rest.jpa.repo.ClientPetRepository;
import com.example.demo.petoffice.rest.jpa.repo.ClientRepository;
import com.example.demo.petoffice.rest.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {

  @Autowired
  ClientRepository clientRepo;

  @Autowired
  private ClientPetRepository cpRepo;

  @Override
  public List<ClientDto> getAllClients() throws PetOfficeExeption {
    List<Client> clients;

    try {
      clients = clientRepo.findAll();
    } catch (Exception e) {
      throw new PetOfficeExeption(PetOfficeErrors.ERROR_FIND_ALL_CLIENTS, e);
    }

    return clients.stream()
        .map(client -> new ClientDto(client.getId(), client.getFirstName(), client.getLastName()))
        .collect(Collectors.toList());
  }

  @Override
  public void adoptPet(PetAdoptionDto petAdoption) throws PetOfficeExeption {
    Client client = findClientById(petAdoption.getClientId());

    cpRepo.save(new ClientPetRef(petAdoption.getPetId(), client,
        Timestamp.valueOf(petAdoption.getRegistered())));
  }

  @Override
  public List<Long> getClientPets(Long id) throws PetOfficeExeption {
    return findClientById(id).getPets().stream().map(cp -> cp.getPetId())
        .collect(Collectors.toList());
  }

  private Client findClientById(long id) throws PetOfficeExeption {
    Optional<Client> client = clientRepo.findById(id);

    if (!client.isPresent())
      throw new PetOfficeExeption(PetOfficeErrors.CLIENT_NOT_FOUND,
          "Client not found for Id #" + id);

    return client.get();
  }
}
