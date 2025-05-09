package com.example.demo.rest.security.auth_server;

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
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.example.demo.rest.security.auth_server.dto.UserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:/test.properties")
public class AuthTest {

  @Autowired
  private TestRestTemplate _rest;

  @Test
  void testLogin() throws JsonMappingException, JsonProcessingException {
    final String userName = "user";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
    map.add("username", userName);
    map.add("password", "password");

    // Login
    HttpEntity<MultiValueMap<String, String>> request =
        new HttpEntity<MultiValueMap<String, String>>(map, headers);
    ResponseEntity<String> response = _rest.postForEntity("/login", request, String.class);
    assertEquals(HttpStatus.FOUND, response.getStatusCode(),
        "Login response status code doesn't match.");

    // Get SSO_SESSION
    String ssoCookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
    String sso = ssoCookie.substring(ssoCookie.indexOf("SSO_SESSION="), ssoCookie.indexOf(";"));

    // Get user info
    UserInfo userInfo = getUserInfo(sso);
    assertEquals(userName, userInfo.name());
    assertEquals(1, userInfo.roles().length);
    assertEquals("ROLE_USER_0", userInfo.roles()[0]);
  }

  private UserInfo getUserInfo(String header) throws JsonMappingException, JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();

    HttpHeaders headers = new HttpHeaders();
    headers.add("Cookie", header);
    @SuppressWarnings({"rawtypes", "unchecked"})
    HttpEntity request = new HttpEntity(null, headers);

    // Get user info
    ResponseEntity<String> response =
        _rest.exchange("/user_info", HttpMethod.GET, request, String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode(),
        "UserInfo response status code doesn't match.");
    // Check that json received and not text/html
    assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType(),
        "Response content type doesn't match.");

    String body = response.getBody();
    assertNotNull(body, "Response body not expected to be null");
    return mapper.readValue(response.getBody(), UserInfo.class);
  }
}
