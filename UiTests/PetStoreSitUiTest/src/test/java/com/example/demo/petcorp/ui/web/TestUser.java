/*
 * Open Source Business Intelligence Tools - http://www.osbitools.com/
 * 
 * Copyright 2014-2018 IvaLab Inc. and by respective contributors (see below).
 * 
 * Released under the LGPL v3 or higher
 * See http://www.gnu.org/licenses/lgpl.txt
 *
 * Contributors:
 * 
 * Igor Peonte <igor.144@gmail.com>
 *
 * Date: @date
 * 
 */
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
