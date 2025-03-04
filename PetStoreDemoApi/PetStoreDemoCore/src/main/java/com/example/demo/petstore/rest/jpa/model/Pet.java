package com.example.demo.petstore.rest.jpa.model;

import com.example.demo.petcorp.shared.jpa.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Pet JPA Model
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
@Entity
@Table
public class Pet extends BaseModel {

  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String sex;

  @Column(nullable = false, length = 1)
  private String vaccinated;

  @Column(nullable = false, length = 1)
  private String adopted = "N";

  /**
   * Default constructor. Required by JPA to work properly since one more custom constructor
   * defined.
   */
  public Pet() {}

  public Pet(String name, String sex, String vaccinated, boolean stf) {
    this.name = name;
    this.setSex(sex);
    this.vaccinated = vaccinated;
    this.setStf(stf);
  }

  public Pet(String name, String sex, String vaccinated) {
    this(name, sex, vaccinated, false);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getVaccinated() {
    return vaccinated;
  }

  public void setVaccinated(String vaccinated) {
    this.vaccinated = vaccinated;
  }

  public String getAdopted() {
    return adopted;
  }

  public void setAdopted(String adopted) {
    this.adopted = adopted;
  }

  public void setVaccinated(boolean isVaccinated) {
    this.vaccinated = isVaccinated ? "Y" : "N";
  }
}
