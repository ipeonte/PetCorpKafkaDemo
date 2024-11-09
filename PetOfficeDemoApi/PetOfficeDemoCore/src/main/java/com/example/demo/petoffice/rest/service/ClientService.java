package com.example.demo.petoffice.rest.service;

import java.util.List;

import com.example.demo.petcorp.shared.dto.PetAdoptionDto;
import com.example.demo.petoffice.rest.dto.ClientDto;
import com.example.demo.petoffice.rest.error.PetOfficeExeption;

/**
 * Interface for Client Service
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
public interface ClientService {

  /**
   * Get all clients
   * 
   * @return All Clients
   * @throws PetOfficeExeption
   */
  List<ClientDto> getAllClients() throws PetOfficeExeption;

  /**
   * Adopt pet and add pet -> client association
   * 
   * @param clientId
   * @param petId
   * @throws PetOfficeExeption
   */
  void adoptPet(PetAdoptionDto petAdoption) throws PetOfficeExeption;

  /**
   * Get Client Pets
   * 
   * @param id Client Id
   * 
   * @return List with Pet Id
   * @throws PetOfficeExeption
   */
  List<Long> getClientPets(Long id) throws PetOfficeExeption;
}
