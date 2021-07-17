package com.example.demo.petcorp.ui.web;

import java.util.HashMap;
import java.util.Map;

/**
 * Test Constants
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
public class SitTestConstants {
  // For demo keep user masks hard-coded
  public static final Map<String, TestUser> USER_MASKS = new HashMap<>();

  static {
    USER_MASKS.put("Test User", new TestUser("user", "0"));
    USER_MASKS.put("Test Pet Holder", new TestUser("pet_keeper", "01"));
    USER_MASKS.put("Test Manager", new TestUser("manager", "11"));
  }
}
