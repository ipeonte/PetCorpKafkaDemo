package com.example.demo.petstore.shared.dto;

/**
 * Class for Pet DTO
 */
public class PetInfoDto extends PetBaseDto {

  // Vaccinated flag. The default is No
  private String vaccinated = "N";

  /**
   * Default constructor
   */
  public PetInfoDto() {
  }

  public PetInfoDto(Long id, String name, PetSex sex, String vaccinated) {
    super(id, name, sex);
    /// DEMO FAIL
    this.vaccinated = vaccinated;
  }

  public PetInfoDto(String name, PetSex sex, String vaccinated) {
    super(null, name, sex);
    this.vaccinated = vaccinated;
  }

  public String getVaccinated() {
    return vaccinated;
  }

  public void setVaccinated(String vaccinated) {
    this.vaccinated = vaccinated;
  }

  @Override
  public String toString() {
    return super.toString() + ":" + vaccinated;
  }
}
