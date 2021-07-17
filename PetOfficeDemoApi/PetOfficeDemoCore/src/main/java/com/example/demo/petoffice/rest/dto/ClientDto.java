package com.example.demo.petoffice.rest.dto;

/**
 * Client Dto
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
public class ClientDto {

  Long id;

  String firstName;

  String lastName;

  /**
   * Default constructor
   */
  public ClientDto() {
  }

  public ClientDto(Long id, String firstName, String lastName) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
