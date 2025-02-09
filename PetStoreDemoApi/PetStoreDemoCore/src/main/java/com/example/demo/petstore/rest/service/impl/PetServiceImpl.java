package com.example.demo.petstore.rest.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import com.example.demo.petcorp.shared.SharedConstants;
import com.example.demo.petcorp.shared.dto.PetAdoptionDto;
import com.example.demo.petstore.rest.common.Constants;
import com.example.demo.petstore.rest.error.PetStoreErrors;
import com.example.demo.petstore.rest.error.PetStoreExeption;
import com.example.demo.petstore.rest.jpa.model.Pet;
import com.example.demo.petstore.rest.jpa.repo.PetsRepository;
import com.example.demo.petstore.rest.service.PetService;
import com.example.demo.petstore.shared.dto.PetBaseDto;
import com.example.demo.petstore.shared.dto.PetInfoDto;
import com.example.demo.petstore.shared.dto.PetSex;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;

/**
 * PetService Implementation
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
@Service
public class PetServiceImpl implements PetService {

  @Autowired
  private PetsRepository petRepo;

  @Autowired
  private StreamBridge streamBridge;

  @Override
  public List<PetBaseDto> getVaccinatedPets(boolean stf) throws PetStoreExeption {
    List<Pet> pets;

    try {
      pets = petRepo.findVaccinatedAdoptedPets("Y", "N", stf);
    } catch (Exception e) {
      throw new PetStoreExeption(PetStoreErrors.ERROR_FIND_VACCINATED_PET, e);
    }

    return pets.stream().map(pet -> new PetBaseDto(pet.getId(), pet.getName(),
        PetSex.valueOf(pet.getSex().toUpperCase()))).collect(Collectors.toList());
  }

  @Override
  public List<PetInfoDto> getAllPets(boolean stf) throws PetStoreExeption {
    List<PetInfoDto> pets = new ArrayList<>();

    try {
      petRepo.findAllByAdoptedOrderByNameAsc("N", stf)
          .forEach(pet -> pets.add(new PetInfoDto(pet.getId(), pet.getName(),
              PetSex.valueOf(pet.getSex().toUpperCase()), pet.getVaccinated())));
    } catch (Exception e) {
      throw new PetStoreExeption(PetStoreErrors.ERROR_GET_ALL_PETS, e);
    }

    return pets;
  }

  @Override
  public PetInfoDto getPetById(Long petId, boolean stf) throws PetStoreExeption {
    return mapPetToPetInfoDto(findPetById(petId, stf));
  }

  @Override
  public PetInfoDto addPet(PetInfoDto pet, boolean stf) throws PetStoreExeption {
    Pet record;

    try {
      record = petRepo.save(new Pet(pet.getName(), pet.getSex().name(), pet.getVaccinated(), stf));
    } catch (Exception e) {
      throw new PetStoreExeption(PetStoreErrors.ERROR_ADD_PET, e);
    }

    return new PetInfoDto(record.getId(), record.getName(), PetSex.valueOf(record.getSex()),
        record.getVaccinated());
  }

  @Override
  public PetBaseDto getVaccinatedPetById(Long petId, boolean stf) throws PetStoreExeption {
    Pet pet;

    try {
      pet = petRepo.findVaccinatedAdoptedPetById(petId, "Y", "N", stf);
    } catch (Exception e) {
      throw new PetStoreExeption(PetStoreErrors.ERROR_GET_VACCINATED_PET_BY_ID, e);
    }

    return (pet != null ? new PetBaseDto(pet.getId(), pet.getName(), PetSex.valueOf(pet.getSex()))
        : null);
  }

  @Override
  public PetInfoDto setVaccinatedStatus(Long id, boolean isVaccinated, boolean stf)
      throws PetStoreExeption {
    Pet pet = findExistingPetById(id, stf);
    pet.setVaccinated(isVaccinated);

    return mapPetToPetInfoDto(petRepo.save(pet));
  }

  @Override
  public void adoptPet(Long petId, Long clientId, boolean stf) throws PetStoreExeption {
    // Before marking pet record as "Adopted" send notification about adopted pet
    Pet adopted = findExistingPetById(petId, stf);

    PetAdoptionDto adoption = mapPetToPetAdoptionDto(petId, clientId, stf);

    // Convert to JSON and send
    String json;
    try {
      json = SharedConstants.MAPPER.writeValueAsString(adoption);
    } catch (JsonProcessingException e) {
      throw new PetStoreExeption(PetStoreErrors.ERROR_CONVERT_ADOP_EVENT, e);
    }

    streamBridge.send(Constants.ADOPT_PET_OUT, json);
    SharedConstants.LOG.info("Sent Pet Adoption Request to downstream systems - " + adoption);

    // Set pet adopted & save
    adopted.setAdopted("Y");
    petRepo.save(adopted);
  }

  private Optional<Pet> findPetById(Long id, boolean stf) throws PetStoreExeption {
    try {
      return petRepo.findPetById(id, stf);
    } catch (Exception e) {
      throw new PetStoreExeption(PetStoreErrors.ERROR_GET_PET_BY_ID, e);
    }
  }

  private Pet findExistingPetById(Long id, boolean stf) throws PetStoreExeption {
    return findPetById(id, stf)
        .orElseThrow(() -> new PetStoreExeption(PetStoreErrors.ERROR_PET_NOT_FOUND,
            "Pet id " + id + " not found."));
  }

  private PetAdoptionDto mapPetToPetAdoptionDto(long petId, long clientId, boolean stf) {
    return new PetAdoptionDto(petId, clientId, stf);
  }

  private PetInfoDto mapPetToPetInfoDto(Optional<Pet> pet) {
    return pet.map(p -> mapPetToPetInfoDto(p)).orElse(null);
  }

  private PetInfoDto mapPetToPetInfoDto(Pet pet) {
    return new PetInfoDto(pet.getId(), pet.getName(), PetSex.valueOf(pet.getSex()),
        pet.getVaccinated());
  }

  @Override
  @Transactional
  public void clearSynthTestData() {
    petRepo.deleteStData();
  }
}
