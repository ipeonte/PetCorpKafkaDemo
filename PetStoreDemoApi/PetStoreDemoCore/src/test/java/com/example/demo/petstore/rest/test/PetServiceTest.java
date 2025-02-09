package com.example.demo.petstore.rest.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import com.example.demo.petstore.rest.error.PetStoreExeption;
import com.example.demo.petstore.rest.service.PetService;
import com.example.demo.petstore.shared.dto.PetBaseDto;
import com.example.demo.petstore.shared.dto.PetInfoDto;
import com.example.demo.petstore.shared.dto.PetSex;

@SpringBootTest
@TestPropertySource("classpath:/test.properties")
public class PetServiceTest {

  @Autowired
  private PetService petService;

  private static final String VACCINATED_FLAG = "Y";

  @Test
  public void testGetAllPetsGood() throws PetStoreExeption {
    List<PetBaseDto> pets = petService.getVaccinatedPets(false);

    assertNotNull(pets);
    assertEquals(2, pets.size());

    // Expected list sort alphabetically in ascending order
    assertEquals("Dino", pets.get(0).getName());
    assertEquals("Lucky", pets.get(1).getName());
    // And with all statuses Available

    pets.forEach(pet -> assertEquals(PetSex.M, pet.getSex()));
  }

  @Test
  public void testGetSynthTest() throws PetStoreExeption {
    List<PetBaseDto> pets = petService.getVaccinatedPets(true);

    assertNotNull(pets);
    assertEquals(0, pets.size());
  }

  @Test
  public void testFindPetByIdPass() throws PetStoreExeption {
    PetBaseDto pet = petService.getPetById(1L, false);

    checkPet(pet, "Lucky", PetSex.M);
  }

  @Test
  public void testFindPetByIdFail() throws PetStoreExeption {
    checkPetNotFound(0L, false);
  }

  @Test
  public void testAddDeletePet() throws PetStoreExeption {
    PetInfoDto pet = petService.addPet(new PetInfoDto("Jira", PetSex.F, VACCINATED_FLAG), false);
    Long id = pet.getId();
    checkPet(petService.getPetById(id, false), "Jira", PetSex.F);

    // Check vaccinated flag
    // DEMO
    try {
      assertEquals("Y", pet.getVaccinated(), "Vaccinated flag doesn't match.");
    } finally {
      petService.adoptPet(id, 0L, false);
    }

    checkPetNotFound(4L, false);
  }

  private void checkPet(PetBaseDto pet, String name, PetSex status) {
    assertNotNull(pet);
    assertEquals(name, pet.getName());
    assertEquals(status, pet.getSex());
  }

  private void checkPetNotFound(long id, boolean stf) throws PetStoreExeption {
    PetBaseDto pet = petService.getVaccinatedPetById(id, stf);
    assertNull(pet, "Pet is not empty.");
  }
}
