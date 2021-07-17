package com.example.demo.petstore.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Generic Test Utils
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
public class GenericTestUtils {

  public static void checkResponseEmpty(ResponseEntity<String> response) {
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNull(response.getBody(), "Response is not empty.");
  }
}
