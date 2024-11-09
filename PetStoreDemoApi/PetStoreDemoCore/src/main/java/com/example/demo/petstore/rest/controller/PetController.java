package com.example.demo.petstore.rest.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.petcorp.shared.SharedConstants;
import com.example.demo.petstore.rest.common.Constants;
import com.example.demo.petstore.rest.error.PetStoreExeption;
import com.example.demo.petstore.rest.service.PetService;
import com.example.demo.petstore.shared.dto.PetBaseDto;
import com.example.demo.petstore.shared.dto.PetInfoDto;

/**
 * PetStore Controller
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */

@RestController
@RequestMapping(SharedConstants.BASE_URL)
public class PetController {

  @Autowired
  private PetService petService;

  /*********************************/
  /** Common Section **/
  /*********************************/

  @GetMapping("/pets")
  public List<PetBaseDto> getPets() throws PetStoreExeption {
    return petService.getVaccinatedPets();
  }

  @GetMapping("/pet/{petId}")
  public PetBaseDto findPetById(@PathVariable Long petId) throws PetStoreExeption {
    PetBaseDto res = petService.getVaccinatedPetById(petId);

    return res;
  }

  /*********************************/
  /** Admin Section **/
  /*********************************/

  @GetMapping(Constants.ADMIN_BASE + "/pets")
  public List<PetInfoDto> getAllPets() throws PetStoreExeption {
    return petService.getAllPets();
  }

  @GetMapping(Constants.ADMIN_BASE + "/pet/{petId}")
  public PetInfoDto findVaccinatedPetById(@PathVariable Long petId) throws PetStoreExeption {
    return petService.getPetById(petId);
  }

  @PostMapping(Constants.ADMIN_BASE + "/pet")
  public PetInfoDto addPet(@Valid @RequestBody PetInfoDto pet) throws PetStoreExeption {
    return petService.addPet(pet);
  }

  @PutMapping(Constants.ADMIN_BASE + "/pet/{petId}/vaccinated")
  public PetInfoDto addPet(@PathVariable Long petId, @Valid @RequestBody Boolean isVaccinated)
      throws PetStoreExeption {
    return petService.setVaccinatedStatus(petId, isVaccinated);
  }

  /*********************************/
  /** Manager Section **/
  /*********************************/

  @PostMapping(Constants.MGR_BASE + "/pet/{petId}/{clientId}")
  public void adoptPet(@PathVariable Long petId, @PathVariable Long clientId)
      throws PetStoreExeption {
    petService.adoptPet(petId, clientId);
  }
}
