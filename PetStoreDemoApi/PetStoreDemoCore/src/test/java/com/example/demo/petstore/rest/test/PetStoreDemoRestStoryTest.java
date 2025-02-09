package com.example.demo.petstore.rest.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import com.example.demo.petcorp.shared.SharedConstants;
import com.example.demo.petcorp.shared.dto.PetAdoptionDto;
import com.example.demo.petstore.rest.common.Constants;
import com.example.demo.petstore.rest.dto.UserInfoDto;
import com.example.demo.petstore.shared.dto.PetBaseDto;
import com.example.demo.petstore.shared.dto.PetInfoDto;
import com.example.rest.common.security.service.UserInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * Behaviour Driven Development (BDD) Example - Test the next user story:
 * 
 * 1. Initially one pet in list is not vaccinated, so .. Check the number of vaccinated pets (as
 * user can see it) and non-vaccinated pet (as admin can see it). Check that numbers are not the
 * same and the second number is bigger. 2. Add new non-vaccinated pet. Check that it's visible for
 * admin only and not visible by user 3. Change vaccinated status to TRUE. Check that it visible now
 * by user, change back and and make sure it's not visible again 4. Adopt pet. Check it not visible
 * by anyone.
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
@TestPropertySource("classpath:/test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PetStoreDemoRestStoryTest {

  @Autowired
  private TestRestTemplate _rest;

  @Autowired
  private OutputDestination output;

  @Test
  public void testAddFindDelRealPet() throws JsonParseException, JsonMappingException, IOException {
    processAddFindDelPet(false, 1);
  }

  @Test
  public void testAddFindDelSynchPet()
      throws JsonParseException, JsonMappingException, IOException {
    processAddFindDelPet(true, 0);

    // Clear synthetic test data
    ResponseEntity<String> response =
        _rest.exchange(Constants.ADMIN_URL + "/clear_st", HttpMethod.DELETE, null, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @SuppressWarnings("unchecked")
  public void processAddFindDelPet(boolean isSynthTest, int inum)
      throws JsonParseException, JsonMappingException, IOException {
    ResponseEntity<String> response;
    String petInfoStr = "\"name\":\"Jira\",\"sex\":\"Female\",\"vaccinated\":\"N\"";

    // 0. Get User Info
    ResponseEntity<UserInfoDto> userInfoResponse =
        _rest.getForEntity(SharedConstants.BASE_URL + "/user_info", UserInfoDto.class);
    assertEquals(HttpStatus.OK, userInfoResponse.getStatusCode(),
        "Invalid response from /user_info");
    UserInfoDto userInfoDto = userInfoResponse.getBody();
    assertNotNull(userInfoDto, "NULL body from /user_info is not expected.");
    UserInfo userInfo = userInfoDto.userInfo();
    assertNotNull(userInfo, "UserInfo response is null");
    assertEquals("0", userInfo.mask(), "UserInfo mask doesn't match.");
    assertEquals("Anonymous", userInfo.name(), "UserInfo name doesn't match.");

    // Value from test.properties
    assertEquals("http://www.example.com/", userInfoDto.breedInfoUrl(),
        "Breed info url doesn't match.");

    // 1.a Get total number of vaccinated pets
    List<PetBaseDto> vpets = (List<PetBaseDto>) _rest
        .getForObject(SharedConstants.BASE_URL + "/pets" + getSynchFlag(isSynthTest), List.class);
    assertNotNull(vpets);
    int vcnt = vpets.size();

    // 1.b Get total number of all pets
    List<PetInfoDto> pets = checkTotalPetCnt(isSynthTest);
    int cnt = pets.size();

    // 1. Check diff in numbers between vaccinated and non-vaccinated
    assertEquals(inum, cnt - vcnt, "Number of vaccinated pet is one less than total.");

    // 2. Add new non-vaccinated pet
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<String>("{" + petInfoStr + "}", headers);

    ResponseEntity<PetInfoDto> resp = _rest.postForEntity(
        Constants.ADMIN_URL + "/pet" + getSynchFlag(isSynthTest), entity, PetInfoDto.class);

    assertEquals(HttpStatus.OK, resp.getStatusCode());

    assertNotNull(resp.getBody());
    long petId = resp.getBody().getId();

    // 3.a Check that new pet is visible for administration
    findPetById(Constants.ADMIN_URL, petId, isSynthTest);

    // 3.b Check that it's not visible for public
    checkPetNotFoundById(SharedConstants.BASE_URL, petId, isSynthTest);

    // 3.c Vaccinate pet
    setPetVaccinatedStatus(petId, Boolean.TRUE, headers, isSynthTest);

    // 3. c Check now it finally visible to public
    findPetById(SharedConstants.BASE_URL, petId, isSynthTest);

    // 3.c Correct vaccinated status since it was put by mistake
    setPetVaccinatedStatus(petId, Boolean.FALSE, headers, isSynthTest);

    // 3.d Check that it again not visible for public
    checkPetNotFoundById(SharedConstants.BASE_URL, petId, isSynthTest);

    // 4. Adopt pet
    response =
        _rest.exchange(Constants.MGR_URL + "/pet/" + petId + "/0" + getSynchFlag(isSynthTest),
            HttpMethod.POST, null, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    // 4. Check the total number of pets are the same
    pets = checkTotalPetCnt(isSynthTest);
    assertEquals(cnt, pets.size());

    // 5. Check if message about pet adoption received.
    byte[] message = output.receive(1000).getPayload();
    PetAdoptionDto pet = SharedConstants.MAPPER.readValue(message, PetAdoptionDto.class);
    assertEquals(petId, pet.getPetId(), "Adopted pet id doesn't match.");
  }

  @SuppressWarnings("unchecked")
  private List<PetInfoDto> checkTotalPetCnt(boolean isSynthTest) {
    List<PetInfoDto> pets;
    pets = (List<PetInfoDto>) _rest
        .getForObject(Constants.ADMIN_URL + "/pets" + getSynchFlag(isSynthTest), List.class);
    assertNotNull(pets);
    return pets;
  }

  private String getSynchFlag(boolean isSynthTest) {
    return isSynthTest ? "?st" : "";
  }

  private PetInfoDto findPetById(String base, Long id, boolean isSynthTest) {
    ResponseEntity<PetInfoDto> response =
        _rest.getForEntity(base + "/pet/" + id + getSynchFlag(isSynthTest), PetInfoDto.class);

    return getPetInfoResponse(response, "Pet " + id + " not found.");
  }

  private PetInfoDto getPetInfoResponse(ResponseEntity<PetInfoDto> response, String message) {
    assertEquals(HttpStatus.OK, response.getStatusCode());
    PetInfoDto pet = response.getBody();
    assertNotNull(pet, message);

    return pet;
  }

  private PetInfoDto checkPetNotFoundById(String base, Long id, boolean isSynthTest) {
    ResponseEntity<PetInfoDto> response =
        _rest.getForEntity(base + "/pet/" + id + getSynchFlag(isSynthTest), PetInfoDto.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    PetInfoDto pet = response.getBody();
    assertNull(pet, "Pet found for Id #" + id);

    return pet;
  }

  private void setPetVaccinatedStatus(Long petId, Boolean status, HttpHeaders headers,
      boolean isSynthTest) {
    HttpEntity<Boolean> vaccinatedStatus = new HttpEntity<Boolean>(status, headers);
    PetInfoDto petInfo = getPetInfoResponse(
        _rest.exchange(
            Constants.ADMIN_URL + "/pet/" + petId + "/vaccinated" + getSynchFlag(isSynthTest),
            HttpMethod.PUT, vaccinatedStatus, PetInfoDto.class),
        "Update vaccinated failed for Pet Id #" + petId);
    assertEquals(petInfo.getVaccinated(), status ? "Y" : "N", "Vaccinated status doesn't match.");
  }
}
