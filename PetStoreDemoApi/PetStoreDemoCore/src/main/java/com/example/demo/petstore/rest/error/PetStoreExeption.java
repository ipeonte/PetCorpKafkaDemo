package com.example.demo.petstore.rest.error;

/**
 * Pet Store Exception
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */

public class PetStoreExeption extends Exception {

  // Default Serial Version UID
  private static final long serialVersionUID = 1L;

  public PetStoreExeption(PetStoreErrors errorCode, Exception e) {
    this(errorCode, e.getMessage());
  }

  public PetStoreExeption(PetStoreErrors errorCode, String message) {
    super(errorCode.name() + " - " + message);
  }
}
