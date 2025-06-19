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

  private final PetStoreErrors error;
  
  private static final String DIVIDER = " - ";
  
  public PetStoreExeption(PetStoreErrors errorCode, Exception e) {
    this(errorCode, e.getMessage());
  }

  public PetStoreExeption(PetStoreErrors errorCode, String message) {
    super(formatMsg(errorCode, message));
    this.error = errorCode;
  }
  
  public static String formatMsg(PetStoreErrors errorCode, String message) {
	  return errorCode.name() + DIVIDER + message;
  }
  
  public PetStoreErrors getErrorCode() {
	return error;
}
}
