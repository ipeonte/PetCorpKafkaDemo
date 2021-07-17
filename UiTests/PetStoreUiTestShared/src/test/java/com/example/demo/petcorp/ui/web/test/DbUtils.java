package com.example.demo.petcorp.ui.web.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.petcorp.ui.web.UiTestConstants;
import com.example.demo.petcorp.ui.web.test.model.Client;

/**
 * 
 * Database Utils
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */

@Service
public class DbUtils {

  @Autowired
  private JdbcTemplate jdbc;

  public void delClientPetRef(int petId) {
    // Remove adopted pet from database
    jdbc.execute("delete from " + UiTestConstants.CLIENT_PET_REF_NAME +
        " where pet_id = " + petId);
  }

  public int checkClientRef(int petId) {
    // Wait for Kafka message to be delivered
    int cnt = 0;
    int count = 0;
    while (count == 0 && cnt < 5) {
      TestUtils.sleep(1);
      count = jdbc.queryForObject(
          "select count(*) from " + UiTestConstants.CLIENT_PET_REF_NAME +
              " where client_id = 1 and pet_id = " + petId,
          Integer.class);
      cnt++;
    }

    // Check pet added into client -> pet ref table
    assertEquals(1, count, "Adopted pet not found in the + " +
        UiTestConstants.CLIENT_PET_REF_NAME + " table");

    return petId;
  }

  public void checkPetAdopted(int petId) {
    // Check pet adopted
    String adopted = jdbc.queryForObject("select adopted from " +
        UiTestConstants.PET_TBL_NAME + " where id = " + petId, String.class);

    assertEquals("Y", adopted, "Adopted Id doesn't match.");
  }

  public void deleteTestPet(int petId) {
    // Remove new pet from database
    jdbc.execute(
        "delete from " + UiTestConstants.PET_TBL_NAME + " where id = " + petId);
  }

  public int getTotalNumVaccPets() {
    return jdbc
        .queryForObject("select count(*) from " + UiTestConstants.PET_TBL_NAME +
            " where vaccinated = 'Y' and adopted = 'N'", Integer.class);
  }

  public int getTotalNumAllPets() {
    return jdbc.queryForObject("select count(*) from " +
        UiTestConstants.PET_TBL_NAME + " where adopted = 'N'", Integer.class);
  }

  public int getPetSearchIdx() throws Exception {
    List<Integer> list = jdbc.queryForList(
        "select id from " + UiTestConstants.PET_TBL_NAME +
            " where vaccinated = 'Y' and adopted = 'N' order by id",
        Integer.class);

    // Size check
    if (list == null || list.size() == 0)
      throw new Exception(UiTestConstants.PET_TBL_NAME + " table is empty.");
    return list.get(0);
  }

  public Client getTestClient() throws Exception {
    List<Integer> list = jdbc.queryForList(
        "select id from " + UiTestConstants.CLIENT_TBL_NAME, Integer.class);

    // Size check
    if (list == null || list.size() == 0)
      throw new Exception(UiTestConstants.CLIENT_TBL_NAME + " table is empty.");

    int id = list.get(0);

    Map<String, Object> result = (Map<String, Object>) jdbc
        .queryForMap("select first_name, last_name from " +
            UiTestConstants.CLIENT_TBL_NAME + " where id = " + id);

    return new Client(id,
        result.get("first_name") + " " + result.get("last_name"));
  }
}
