package com.example.demo.petoffice.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.TestPropertySource;

import com.example.demo.petcorp.shared.SharedConstants;
import com.example.demo.petcorp.shared.dto.PetAdoptionDto;
import com.example.demo.petoffice.rest.jpa.model.ClientPetRef;
import com.example.demo.petoffice.rest.jpa.repo.ClientPetRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@TestPropertySource("classpath:/test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PetOfficeStoryTest {

  @Autowired
  private TestRestTemplate _rest;

  @Autowired
  private InputDestination input;

  @Autowired
  private ClientPetRepository cpRepo;

  @Test
  void testGetAllCustomers() {
    List<?> clients =
        _rest.getForObject(SharedConstants.BASE_URL + "/clients", List.class);
    assertNotNull(clients);
    assertEquals(2, clients.size());
  }

  /**
   * 1. Push adoption pet event
   * 2. Check if message about pet adoption received and inserted into ClientPetRef table
   * 3. Check if client has a new adopted pet
   * 
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  @Test
  public void testPetAdoption()
      throws JsonParseException, JsonMappingException, IOException {
    final long petId = 1;
    final long clientId = 2;

    // 0. Check client-> pet ref is empty before test
    getClientPetsList(clientId, 0);

    PetAdoptionDto petInfo = new PetAdoptionDto(petId, clientId);
    petInfo.setRegistered(LocalDateTime.now());
    assertNotNull(petInfo.getRegistered(), "Registered field not empty");

    // 1. Push adoption pet event
    this.input.send(new GenericMessage<String>(
        SharedConstants.MAPPER.writeValueAsString(petInfo)));

    // 2. Check if message about pet adoption received and inserted into ClientPetRef table
    List<ClientPetRef> list = cpRepo.findAll();
    assertNotNull(list, "List of ClientPetRef is empty.");
    assertEquals(1, list.size(), "Size of ClientPetRef list doesn't match.");

    ClientPetRef cp = list.get(0);
    assertEquals(petId, cp.getPetId(), "Pet Id doesn't match.");
    assertEquals(clientId, cp.getClient().getId(), "Client Id doesn't match.");
    Timestamp registered = cp.getRegistered();
    assertNotNull(registered, "ClientPetRef registered is null");
    long diff = System.currentTimeMillis() - registered.getTime();

    // Expected difference less than 1 second
    assertTrue(diff <= 1000, "Difference in registered field more than 1 sec.");
    List<Integer> pets = getClientPetsList(clientId, 1);
    int id = pets.get(0).intValue();
    assertEquals(petId, id, "Adopted pet id doesn't match.");
  }

  private List<Integer> getClientPetsList(long clientId, int size) {
    @SuppressWarnings("unchecked")
    List<Integer> pets = _rest.getForObject(
        SharedConstants.BASE_URL + "/clients/" + clientId + "/pets",
        List.class);

    assertNotNull(pets, "List of pets is NULL");
    assertEquals(size, pets.size(), "pets list size doesn't match.");

    return pets;
  }
}
