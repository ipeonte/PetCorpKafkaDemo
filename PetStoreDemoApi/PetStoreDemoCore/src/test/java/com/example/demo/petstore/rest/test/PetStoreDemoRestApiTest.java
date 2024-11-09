package com.example.demo.petstore.rest.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import com.example.demo.petcorp.shared.SharedConstants;
import com.example.demo.petstore.rest.GenericTestUtils;
import com.example.demo.petstore.rest.common.Constants;
import com.example.demo.petstore.shared.dto.PetBaseDto;

/**
 * Test Driven Development (TDD) Example - Test Pets Demo Store API
 */
@DirtiesContext
@TestPropertySource("classpath:/test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PetStoreDemoRestApiTest {

  @Autowired
  private TestRestTemplate _rest;

  @Autowired
  private JdbcTemplate _jdbc;

  @Test
  public void testFindPetBad() {
    GenericTestUtils.checkResponseEmpty(_rest
        .getForEntity(SharedConstants.BASE_URL + "/pet/100", String.class));
    GenericTestUtils.checkResponseEmpty(
        _rest.getForEntity(Constants.ADMIN_URL + "/pet/100", String.class));

    checkBadUrl(SharedConstants.BASE_URL + "/pet/a", HttpStatus.BAD_REQUEST);
  }

  @Test
  public void testFindPetGood() {
    ResponseEntity<String> response =
        _rest.getForEntity(SharedConstants.BASE_URL + "/pet/1", String.class);

    assertEquals(HttpStatus.OK, response.getStatusCode(),
        "Response code from /pet/1 search doesn't match");

    assertNotNull(response.getBody());
    assertEquals("{\"id\":1,\"name\":\"Lucky\",\"sex\":\"Male\"}",
        response.getBody());
  }

  @Test
  public void testAddPetBad() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Try empty request
    ResponseEntity<String> response =
        _rest.postForEntity(Constants.ADMIN_URL + "/pet",
            new HttpEntity<String>("", headers), String.class);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    // Try invalid payload
    response = _rest.postForEntity(Constants.ADMIN_URL + "/pet",
        new HttpEntity<String>("{\"name\": \"Jira\"}", headers), String.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void testAddPetGood() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<String>(
        "{\"name\": \"Jira\",\"sex\":\"Female\"}", headers);

    ResponseEntity<PetBaseDto> response = _rest
        .postForEntity(Constants.ADMIN_URL + "/pet", entity, PetBaseDto.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());

    assertNotNull(response.getBody());
    long id = response.getBody().getId();

    // Check entry available in database
    assertEquals(1,
        _jdbc.queryForObject("select count(*) from " + "pet where id = " + id,
            Integer.class).intValue());

    // Delete that entry
    _jdbc.execute("delete from pet where id = " + id);

  }

  @Test
  public void testAdoptBad() {
    ResponseEntity<String> response =
        _rest.exchange(Constants.MGR_URL + "/pet/100/0", HttpMethod.POST,
            null, String.class);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    response = _rest.exchange(Constants.MGR_URL + "/pet/a/0", HttpMethod.POST,
        null, String.class);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void testAdoptPetGood() {
    int id = 99;
    // Insert new record
    _jdbc
        .execute("insert into pet(id, name, sex, vaccinated, adopted) values(" +
            id + ", 'Jira', 'F', 'Y', 'N')");

    // Delete that entry
    ResponseEntity<String> response =
        _rest.exchange(Constants.MGR_URL + "/pet/" + id + "/0",
            HttpMethod.POST, null, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    // Check entry doesn't exists in database any more
    assertEquals(1, _jdbc.queryForObject(
        "select count(*) from " + "pet where id = " + id + " and adopted = 'Y'",
        Integer.class).intValue());
  }

  private void checkBadUrl(String url, HttpStatus responseCode) {
    ResponseEntity<String> response = _rest.getForEntity(url, String.class);
    assertEquals(responseCode, response.getStatusCode());

  }
}
