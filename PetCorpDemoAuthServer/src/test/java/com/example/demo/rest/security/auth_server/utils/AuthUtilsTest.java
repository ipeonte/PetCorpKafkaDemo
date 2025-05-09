package com.example.demo.rest.security.auth_server.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class AuthUtilsTest {

  @Test
  void testCalcUserMask() {
    assertEquals(0, AuthUtils.calcUserMask("USER_0"));
    assertEquals(1, AuthUtils.calcUserMask("PET_KEEPER_01"));
    assertEquals(2, AuthUtils.calcUserMask("MANAGER_10"));
    assertEquals(3, AuthUtils.calcUserMask("ADMIN_11"));
    assertEquals(3, AuthUtils.calcUserMask(new String[] {"PET_KEEPER_01", "MANAGER_10"}));
  }

}
