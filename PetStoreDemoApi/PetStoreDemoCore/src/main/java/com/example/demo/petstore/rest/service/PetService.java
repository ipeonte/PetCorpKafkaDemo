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
   * @param stf flag for Synthetic Test
   * 
   * @return All Pets
   * @throws PetStoreExeption
   */
  List<PetInfoDto> getAllPets(boolean stf) throws PetStoreExeption;

  /**
   * Get only vaccinated pets
   * 
   * @param stf flag for Synthetic Test
   * 
   * @return All Pets
   * @throws PetStoreExeption
   */
  List<PetBaseDto> getVaccinatedPets(boolean stf) throws PetStoreExeption;

  /**
   * Find Pet by Pet Id
   * 
   * @param id Pet Id
   * @param stf flag for Synthetic Test
   * 
   * @return AllPetInfoDto object or null if nothing found
   * @throws PetStoreExeption
   */
  PetInfoDto getPetById(Long id, boolean stf) throws PetStoreExeption;

  /**
   * Add new Pet
   * 
   * @param pet AllPetInfoDto Object
   * @param stf flag for Synthetic Test
   * 
   * @return Pet Id for newly added pet
   * @throws PetStoreExeption
   */
  PetInfoDto addPet(PetInfoDto pet, boolean stf) throws PetStoreExeption;

  /**
   * Get Vaccinated Pet by Id
   * 
   * @param petId Pet Id
   * @param stf flag for Synthetic Test
   * 
   * @return PetDto
   * @throws PetStoreExeption
   */
  PetBaseDto getVaccinatedPetById(Long petId, boolean stf) throws PetStoreExeption;

  /**
   * Set pet vaccinated
   * 
   * @param id Pet Id
   * @param stf flag for Synthetic Test
   * 
   * @return Pet Info DTO
   * 
   * @throws PetStoreExeption
   */
  PetInfoDto setVaccinatedStatus(Long id, boolean isVaccinated, boolean stf)
      throws PetStoreExeption;

  /**
   * Adopt pet by Id
   * 
   * @param petId Pet Id
   * @param clientId Client Id
   * @param stf flag for Synthetic Test
   * 
   * @throws PetStoreExeption
   */
  void adoptPet(Long petId, Long clientId, boolean stf) throws PetStoreExeption;

  /**
   * Clear data from synthetic tests
   */
  void clearSynthTestData();
}
