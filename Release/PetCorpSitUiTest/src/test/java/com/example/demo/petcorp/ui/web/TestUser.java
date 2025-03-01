package com.example.demo.petcorp.ui.web;

/**
 * POJO for Test User Info
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
public class TestUser {

  private final String userName;

  private final String mask;

  public TestUser(String userName, String mask) {
    this.userName = userName;
    this.mask = mask;
  }

  public String getUserName() {
    return userName;
  }

  public String getMask() {
    return mask;
  }
}
