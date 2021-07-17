package com.example.demo.petstore.rest.service;

import java.util.List;

import com.example.demo.petstore.rest.error.PetStoreExeption;
import com.example.demo.petstore.shared.dto.PetBaseDto;
import com.example.demo.petstore.shared.dto.PetInfoDto;

/**
 * Interface for Pet Service
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
public interface PetService {

  /**
   * Get all pets
   * 
   * @return All Pets
   * @throws PetStoreExeption 
   */
  List<PetInfoDto> getAllPets() throws PetStoreExeption;

  /**
   * Get only vaccinated pets
   * 
   * @return All Pets
   * @throws PetStoreExeption 
   */
  List<PetBaseDto> getVaccinatedPets() throws PetStoreExeption;

  /**
   * Find Pet by Pet Id
   * 
   * @param id Pet Id
   * @return AllPetInfoDto object or null if nothing found
   * @throws PetStoreExeption 
   */
  PetInfoDto getPetById(Long id) throws PetStoreExeption;

  /**
   * Add new Pet
   * 
   * @param pet AllPetInfoDto Object
   * 
   * @return Pet Id for newly added pet
   * @throws PetStoreExeption 
   */
  PetInfoDto addPet(PetInfoDto pet) throws PetStoreExeption;

  /**
   * Get Vaccinated Pet by Id
   * 
   * @param petId Pet Id
   * 
   * @return PetDto
   * @throws PetStoreExeption 
   */
  PetBaseDto getVaccinatedPetById(Long petId) throws PetStoreExeption;

  /**
   * Set pet vaccinated
   * 
   * @param id Pet Id
   * @return Pet Info DTO
   * 
   * @throws PetStoreExeption
   */
  PetInfoDto setVaccinatedStatus(Long id, boolean isVaccinated)
      throws PetStoreExeption;

  /**
   * Adopt pet by Id
   * 
   * @param petId Pet Id
   * @param clientId Client Id
   * 
   * @throws PetStoreExeption 
   */
  void adoptPet(Long petId, Long clientId) throws PetStoreExeption;
}
