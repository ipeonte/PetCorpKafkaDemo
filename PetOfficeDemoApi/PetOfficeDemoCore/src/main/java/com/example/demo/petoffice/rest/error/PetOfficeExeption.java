package com.example.demo.petoffice.rest.error;

/**
 * Pet Office Exception
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */

public class PetOfficeExeption extends Exception {

  // Default Serial Version UID
  private static final long serialVersionUID = 1L;

  public PetOfficeExeption(PetOfficeErrors errorCode, Exception e) {
    this(errorCode, e.getMessage());
  }

  public PetOfficeExeption(PetOfficeErrors errorCode, String message) {
    super(errorCode.name() + " - " + message);
  }
}
