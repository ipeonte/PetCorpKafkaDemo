package com.example.demo.petstore.rest.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;

import com.example.demo.petstore.shared.dto.PetSex;
import com.example.demo.petstore.rest.error.PetStoreExeption;
import com.example.demo.petstore.rest.service.PetService;
import com.example.demo.petstore.shared.dto.PetInfoDto;
import com.example.demo.petstore.shared.dto.PetBaseDto;

@TestPropertySource("classpath:/test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class PetServiceTest {

  @Autowired
  private PetService petService;

  private static final String VACCINATED_FLAG = "Y";

  @Test
  public void testGetAllPetsGood() throws PetStoreExeption {
    List<PetBaseDto> pets = petService.getVaccinatedPets();

    assertNotNull(pets);
    assertEquals(2, pets.size());

    // Expected list sort alphabetically in ascending order
    assertEquals("Dino", pets.get(0).getName());
    assertEquals("Lucky", pets.get(1).getName());
    // And with all statuses Available

    pets.forEach(pet -> assertEquals(PetSex.M, pet.getSex()));
  }

  @Test
  public void testFindPetByIdPass() throws PetStoreExeption {
    PetBaseDto pet = petService.getPetById(1L);

    checkPet(pet, "Lucky", PetSex.M);
  }

  @Test
  public void testFindPetByIdFail() throws PetStoreExeption {
    checkPetNotFound(0L);
  }

  @Test
  public void testAddDeletePet() throws PetStoreExeption {
    PetInfoDto pet =
        petService.addPet(new PetInfoDto("Jira", PetSex.F, VACCINATED_FLAG));
    Long id = pet.getId();
    checkPet(petService.getPetById(id), "Jira", PetSex.F);

    // Check vaccinated flag
    // DEMO
    try {
      assertEquals("Y", pet.getVaccinated(), "Vaccinated flag doesn't match.");
    } finally {
      petService.adoptPet(id, 0L);
    }

    checkPetNotFound(4L);
  }

  private void checkPet(PetBaseDto pet, String name, PetSex status) {
    assertNotNull(pet);
    assertEquals(name, pet.getName());
    assertEquals(status, pet.getSex());
  }
  
  private void checkPetNotFound(long id) throws PetStoreExeption {
    PetBaseDto pet = petService.getVaccinatedPetById(id);
    assertNull(pet, "Pet is not empty.");
  }
}
