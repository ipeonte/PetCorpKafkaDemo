package com.example.demo.petcorp.shared.dto;

import java.time.LocalDateTime;

/**
 * Class for Pet DTO
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 */
public class PetAdoptionDto {

  // Pet Id
  private Long petId;

  // Client Id
  private Long clientId;

  // Timestamp when adoption was registered
  private LocalDateTime registered;

  /**
   * Default constructor
   */
  public PetAdoptionDto() {}

  public PetAdoptionDto(Long petId, Long clientId) {
    this.petId = petId;
    this.clientId = clientId;
  }

  public Long getPetId() {
    return petId;
  }

  public void setPetId(Long petId) {
    this.petId = petId;
  }

  public Long getClientId() {
    return clientId;
  }

  public void setClientId(Long clientId) {
    this.clientId = clientId;
  }

  public LocalDateTime getRegistered() {
    return registered;
  }

  public void setRegistered(LocalDateTime registered) {
    this.registered = registered;
  }

  @Override
  public String toString() {
    return petId + ":" + clientId + ":" + registered;
  }
}
