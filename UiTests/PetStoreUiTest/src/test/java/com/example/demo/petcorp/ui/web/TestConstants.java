package com.example.demo.petcorp.ui.web;

import java.util.HashMap;
import java.util.Map;

import com.example.demo.petstore.rest.jpa.model.Pet;

/**
 * Test Constants
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
public class TestConstants {
  public static final Map<String, String> USER_MASKS = new HashMap<>();

  // Name of pet table
  public static final String PET_TBL_NAME =
      Pet.class.getSimpleName().toLowerCase();

  static {
    USER_MASKS.put("Test User", "0");
    USER_MASKS.put("Test Pet Holder", "01");
    USER_MASKS.put("Test Manager", "11");
  }
}
