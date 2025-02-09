package com.example.demo.petstore.rest.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.petcorp.shared.SharedConstants;
import com.example.demo.petstore.rest.common.Constants;
import com.example.demo.petstore.rest.error.PetStoreExeption;
import com.example.demo.petstore.rest.service.PetService;
import com.example.demo.petstore.shared.dto.PetBaseDto;
import com.example.demo.petstore.shared.dto.PetInfoDto;
import jakarta.validation.Valid;

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
  public List<PetBaseDto> getPets(@RequestParam(required = false) String st)
      throws PetStoreExeption {
    return petService.getVaccinatedPets(st != null);
  }

  @GetMapping("/pet/{petId}")
  public PetBaseDto findPetById(@PathVariable Long petId, @RequestParam(required = false) String st)
      throws PetStoreExeption {
    PetBaseDto res = petService.getVaccinatedPetById(petId, st != null);

    return res;
  }

  /*********************************/
  /** Admin Section **/
  /*********************************/

  @GetMapping(Constants.ADMIN_BASE + "/pets")
  public List<PetInfoDto> getAllPets(@RequestParam(required = false) String st)
      throws PetStoreExeption {
    return petService.getAllPets(st != null);
  }

  @GetMapping(Constants.ADMIN_BASE + "/pet/{petId}")
  public PetInfoDto findVaccinatedPetById(@PathVariable Long petId,
      @RequestParam(required = false) String st) throws PetStoreExeption {
    return petService.getPetById(petId, st != null);
  }

  @PostMapping(Constants.ADMIN_BASE + "/pet")
  public PetInfoDto addPet(@Valid @RequestBody PetInfoDto pet,
      @RequestParam(required = false) String st) throws PetStoreExeption {
    return petService.addPet(pet, st != null);
  }

  @PutMapping(Constants.ADMIN_BASE + "/pet/{petId}/vaccinated")
  public PetInfoDto addPet(@PathVariable Long petId, @Valid @RequestBody Boolean isVaccinated,
      @RequestParam(required = false) String st) throws PetStoreExeption {
    return petService.setVaccinatedStatus(petId, isVaccinated, st != null);
  }

  @DeleteMapping(Constants.ADMIN_BASE + "/clear_st")
  public void clearSynthTestData() throws PetStoreExeption {
    petService.clearSynthTestData();
  }

  /*********************************/
  /** Manager Section **/
  /*********************************/

  @PostMapping(Constants.MGR_BASE + "/pet/{petId}/{clientId}")
  public void adoptPet(@PathVariable Long petId, @PathVariable Long clientId,
      @RequestParam(required = false) String st) throws PetStoreExeption {
    petService.adoptPet(petId, clientId, st != null);
  }
}
