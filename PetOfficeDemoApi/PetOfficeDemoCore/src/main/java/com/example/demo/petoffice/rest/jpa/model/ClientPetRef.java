package com.example.demo.petoffice.rest.jpa.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class ClientPetRef {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  private Client client;

  @Column(nullable = false)
  private Long petId;

  @Column
  private Timestamp registered;

  /**
   * Default constructor. Required by JPA to work properly since one more custom constructor
   * defined.
   */
  public ClientPetRef() {}

  public ClientPetRef(Long petId, Client client, Timestamp registered) {
    this.petId = petId;
    this.client = client;
    this.registered = registered;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public Long getPetId() {
    return petId;
  }

  public void setPetId(Long petId) {
    this.petId = petId;
  }

  public Timestamp getRegistered() {
    return registered;
  }

  public void setRegistered(Timestamp registered) {
    this.registered = registered;
  }
}
