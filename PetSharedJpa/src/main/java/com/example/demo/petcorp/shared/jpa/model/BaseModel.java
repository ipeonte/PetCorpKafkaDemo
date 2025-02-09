package com.example.demo.petcorp.shared.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseModel {

  @Column
  private Boolean stf = false;

  public Boolean getStf() {
    return stf;
  }

  public void setStf(Boolean isSynchTest) {
    this.stf = isSynchTest;
  }
}
