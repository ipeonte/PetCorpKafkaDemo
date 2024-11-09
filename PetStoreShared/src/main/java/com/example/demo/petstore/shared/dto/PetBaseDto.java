package com.example.demo.petstore.shared.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Class for Pet DTO
 */
public class PetBaseDto {

  private Long id;

  @NotBlank
  private String name;

  @NotNull
  private PetSex sex;

  /**
   * Default constructor
   */
  public PetBaseDto() {
  }

  public PetBaseDto(Long id, String name, PetSex sex) {
    this.id = id;
    this.name = name;
    this.sex = sex;
  }

  public PetBaseDto(String name, PetSex sex) {
    this(null, name, sex);
  }

  /**
   * Get id
   * 
   * @return id
   **/
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Get name
   * 
   * @return name
   **/
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * pet status in the store
   * 
   * @return status
   **/
  public PetSex getSex() {
    return sex;
  }

  public void setSex(PetSex sex) {
    this.sex = sex;
  }

  @Override
  public String toString() {
    return id + ":" + name + ":" + getSex();
  }
}
