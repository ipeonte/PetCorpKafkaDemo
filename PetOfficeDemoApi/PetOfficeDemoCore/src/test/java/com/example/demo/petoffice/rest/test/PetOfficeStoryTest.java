package com.example.demo.petoffice.rest.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.TestPropertySource;
import com.example.demo.petcorp.shared.SharedConstants;
import com.example.demo.petcorp.shared.dto.PetAdoptionDto;
import com.example.demo.petoffice.rest.jpa.model.ClientPetRef;
import com.example.demo.petoffice.rest.jpa.repo.ClientPetRepository;
import com.example.demo.petoffice.rest.jpa.repo.ClientRepository;
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

  @Autowired
  ClientRepository clientRepo;

  @BeforeEach
  public void clear() {
    // Clear ClientPetRef table
    cpRepo.deleteAll();
  }

  @Test
  void testGetAllCustomers() {
    List<?> clients = _rest.getForObject(SharedConstants.BASE_URL + "/clients", List.class);
    assertNotNull(clients);
    assertEquals(2, clients.size());
  }

  /**
   * 1. Push adoption pet event 2. Check if message about pet adoption received and inserted into
   * ClientPetRef table 3. Check if client has a new adopted pet
   * 
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  @Test
  public void testPetAdoption() throws JsonParseException, JsonMappingException, IOException {
    final long petId = 1;
    final long clientId = 2;

    // 0. Check client-> pet ref is empty before test
    getClientPetsList(clientId, 0);

    PetAdoptionDto petInfo = new PetAdoptionDto(petId, clientId, false);
    petInfo.setRegistered(LocalDateTime.now());
    assertNotNull(petInfo.getRegistered(), "Registered field not empty");

    // 1. Push adoption pet event
    this.input.send(new GenericMessage<String>(SharedConstants.MAPPER.writeValueAsString(petInfo)));

    // 2. Check if message about pet adoption received and inserted into ClientPetRef table
    List<ClientPetRef> list = checkClientPetRef(1);

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

  @Test
  public void testClearSynthData() {
    // Clear synthetic test data
    ResponseEntity<String> response = _rest.exchange(SharedConstants.BASE_URL + "/clients/clear_st",
        HttpMethod.DELETE, null, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testInvalidMessage() {
    // Send invalid message
    this.input.send(new GenericMessage<String>("test"));

    // Check no record added to the ClientPetRef table
    checkClientPetRef(0);
  }

  @Test
  public void testInvalidScenario() {
    // Test for non-existing client
    ResponseEntity<String> resp =
        _rest.getForEntity(SharedConstants.BASE_URL + "/clients/0/pets", String.class);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());

    // Expected no data for synthetic tests
    // getClientPetsList(0, 0, true);
  }

  private List<ClientPetRef> checkClientPetRef(int size) {
    List<ClientPetRef> list = cpRepo.findAll();
    assertNotNull(list, "List of ClientPetRef is empty.");
    assertEquals(size, list.size(), "Size of ClientPetRef list doesn't match.");
    return list;
  }

  private List<Integer> getClientPetsList(long clientId, int size) {
    return getClientPetsList(clientId, size, false);
  }

  private List<Integer> getClientPetsList(long clientId, int size, boolean stf) {
    @SuppressWarnings("unchecked")
    List<Integer> pets = _rest.getForObject(
        SharedConstants.BASE_URL + "/clients/" + clientId + "/pets" + (stf ? "?st=" : ""),
        List.class);

    assertNotNull(pets, "List of pets is NULL");
    assertEquals(size, pets.size(), "pets list size doesn't match.");

    return pets;
  }
}
