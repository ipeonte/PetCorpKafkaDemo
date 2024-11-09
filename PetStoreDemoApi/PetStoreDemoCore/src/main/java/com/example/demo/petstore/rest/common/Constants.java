package com.example.demo.petstore.rest.common;

import com.example.demo.petcorp.shared.SharedConstants;

/**
 * Global Constants
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
public class Constants {

  public static final String ADMIN_BASE = "/admin";

  public static final String ADMIN_URL = SharedConstants.BASE_URL + ADMIN_BASE;

  public static final String MGR_BASE = "/mgr";

  public static final String MGR_URL = SharedConstants.BASE_URL + MGR_BASE;

  // Binding for pet adoption topic
  public static final String ADOPT_PET_OUT = "adopt-pet";
}
