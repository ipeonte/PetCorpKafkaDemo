package com.example.demo.petcorp.ui.web.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.petcorp.ui.web.UiTestConstants;
import com.example.demo.petcorp.ui.web.test.model.Client;

public abstract class AbstractPetStoreDemoUiTest {

  @Autowired
  private DbUtils dbUtils;

  // Web driver
  private WebDriver driver;

  private int vaccPetsNum;

  private int allPetsNum;

  private int petSearchIdx;

  // Web navigation elements
  private WebElement allPets;
  private WebElement petTable;
  private WebElement searchForm;
  private WebElement searchInput;
  private WebElement searchBtn;
  private WebElement resetBtn;
  private WebElement addPetBtn;
  private WebElement adoptPetBtn;

  protected abstract String preAuthUser(String userName);

  protected abstract String postAuthUser(String userName);

  protected abstract String getBaseUrl();

  @BeforeEach
  public void setUp() throws Exception {
    // Set download folder
    File f = new File("target");

    Map<String, Object> chromePrefs = new HashMap<String, Object>();
    chromePrefs.put("download.default_directory", f.getAbsolutePath());

    ChromeOptions options = new ChromeOptions();
    options.setExperimentalOption("prefs", chromePrefs);
    // options.addArguments("start-maximized"); Doesn't work in headless mode
    options.addArguments("disable-infobars");
    // Enable if headless execution is needed
    // options.addArguments("headless");

    // Set Chrome Driver    
    System.setProperty("webdriver.chrome.driver", "./driver/" + getChromeDriverName());
    System.setProperty("webdriver.chrome.silentOutput", "true");

    driver = new ChromeDriver(options);
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));

    // Set size manually
    driver.manage().window().setSize(new Dimension(1920, 1080));

    // Get the total number of vaccinated pets
    vaccPetsNum = dbUtils.getTotalNumVaccPets();

    // Get the total number of pets
    allPetsNum = dbUtils.getTotalNumAllPets();

    // Pet Id to search
    petSearchIdx = dbUtils.getPetSearchIdx();
  }

  /**
   * Test Case description:
   * 
   * 1. Login as user 2. Check that 3 records visible 3. Enter Pet Id -> Click search 4. Check that
   * only 1 record is visible 5. Click Reset and repeat step #2
   */
  @Test
  public void testUserSearch() {
    // Set user role and check all pet records visible
    init("Test User");

    // Search for Pet Id
    searchPetById(petSearchIdx, 1);

    // Reset search
    resetSearch();

    driver.close();
  }

  /**
   * Test Case description:
   * 
   * 1. Login as pet handler 2. Check that 3 records visible 3. Add new pet 4. Check that 4 records
   * visible 5. Do pet search for new pet 6. Remove new pet from DB
   */
  @Test
  public void testAddPet() {
    // Set Pet Handler role and check all pet records visible
    init("Test Pet Holder");

    // Add new pet and check that extra row is visible
    int petId = addVaccinatedTestPet();

    // Remove new pet from database
    deleteTestPetClose(petId);
  }

  /**
   * Test Case description:
   * 
   * 1. Login as manager 2. Check that 3 records visible 3. Execute testAddPet() 4. Delete new pet,
   * search and check that no records returned
   * 
   * @throws Exception
   */
  @Test
  public void testAdoptPet() throws Exception {
    // Set manager role and check all pet records visible
    init("Test Manager");

    // Add pet
    int petId = addVaccinatedTestPet();

    // Adopt pet
    adoptPet(petId);

    // Remove new pet from database
    deleteTestPetClose(petId);
  }

  private String getChromeDriverName() {
	  String os = System.getProperty("os.name").toLowerCase();
	  String fname =  os.contains("win") ? "chromedriver.exe" : (os.contains("nix") || os.contains("nux")
              || os.contains("aix") ? "chromedriver" : null);
	  
	  if (fname == null)
		  throw new RuntimeException();
	  
	  return fname;
  }
  
  private void adoptPet(int petId) throws Exception {
    // Click on selected
    driver.findElement(By.cssSelector("input[type=checkbox]")).click();

    Client testClient = dbUtils.getTestClient();

    // Choose customer
    WebElement client = driver.findElement(By.id("client_list"));
    new Select(client).selectByVisibleText(testClient.getName());

    adoptPetBtn.click();
    assertEquals("Please confirm adoption of 1 pet by " + testClient.getName(), acceptAlert(),
        "Alert text for accept adopted pet doesn't match.");
    TestUtils.sleep();

    // Check pet disappear from list
    checkPetRecordsVisible(0, 2);
    resetBtn.click();
    TestUtils.sleep();
    checkPetRecordsVisible(allPetsNum, 2);

    checkPetAdopted(petId);

    delClientPetRef(checkClientRef(petId));
  }

  private int addVaccinatedTestPet() {
    return addPet(UiTestConstants.TEST_PET_NAME, UiTestConstants.TEST_PET_SEX, true);
  }

  private int addPet(String petName, String petSex, boolean isVaccinated) {
    // Check initial pets visible
    checkPetRecordsVisible(allPetsNum, 2);

    addPetBtn.click();

    WebElement newPetForm = driver.findElement(By.className("new-pet"));

    // Check submit button is disabled
    WebElement submit = newPetForm.findElement(By.className("btn-primary"));
    assertFalse(submit.isEnabled(), "Submit button should not be enabled on new 'Add Pet' form.");

    List<WebElement> inputs = newPetForm.findElements(By.tagName("input"));
    inputs.get(0).clear();
    inputs.get(0).sendKeys(petName);

    // Total 2 selectors - one for pet sex and another for vaccinated flag
    List<WebElement> selectors = newPetForm.findElements(By.tagName("select"));
    Select select = new Select(selectors.get(0));
    select.selectByVisibleText(petSex);

    // Set vaccinated
    select = new Select(selectors.get(1));
    select.selectByVisibleText(isVaccinated ? "Yes" : "No");

    // Submit form
    TestUtils.sleep(2);
    submit.click();
    TestUtils.sleep(2);

    // Check more pets visible
    checkPetRecordsVisible(allPetsNum + 1, 2);

    // Search for new pet id. Use hard-coded id for now
    int id = findNewPet();
    List<WebElement> pets = searchPetById(id, 2);

    /// DEMO
    WebElement pet = pets.get(1).findElement(By.className("pet-vaccinated"));
    assertEquals("Yes", pet.getText(), "Vaccinated flag doesn't match.");

    return id;
  }

  /**
   * Return id of last element in the table
   * 
   * @return the id for new pet
   */
  private int findNewPet() {
    List<WebElement> list = listPets(petTable);
    WebElement cell = list.get(list.size() - 2).findElement(By.tagName("td"));
    return Integer.parseInt(cell.getText());
  }

  private void resetSearch() {
    resetBtn.click();
    TestUtils.sleep(2);
    checkPetRecordsVisible(vaccPetsNum, 1);
  }

  private void init(String userRole) {
    String textMask = preAuthUser(userRole);

    driver.get(getBaseUrl());
    TestUtils.sleep(2);

    if (textMask == null)
      textMask = postAuthUser(userRole);

    allPets = driver.findElement(By.className("all-pets"));
    petTable = allPets.findElement(By.tagName("table"));
    searchForm = allPets.findElement(By.className("form-inline"));
    searchInput = searchForm.findElement(By.tagName("input"));
    searchBtn = searchForm.findElement(By.className("btn-primary"));
    resetBtn = searchForm.findElement(By.className("btn-secondary"));

    // Based on user role check that some elements visible and some not

    // Calculate numeric mask
    int mask = 0;
    int shift = 1;
    for (int i = textMask.length(); i > 0; i--)
      mask += textMask.charAt(i - 1) == '1' ? shift << textMask.length() - i : 0;

    // Check all pets visible
    checkPetRecordsVisible(mask == 0 ? vaccPetsNum : allPetsNum, mask == 0 ? 1 : 2);

    // Add pet only visible for pet handlers and manager
    try {
      addPetBtn = petTable.findElement(By.className("btn-primary"));
    } catch (NoSuchElementException e) {
      // Expected only for user
      assertTrue(mask == 0, "Button 'Add Pet' not found for user '" + userRole + "'");
    }

    // Adopt pet only visible for manager
    try {
      adoptPetBtn = petTable.findElement(By.id("adopt_pet"));

      // Initially disabled
      assertEquals("true", adoptPetBtn.getAttribute("disabled"),
          "Adopt Pet button is not disabled.");
    } catch (NoSuchElementException e) {
      // Expected only for user
      assertTrue(mask != 3, "Button 'Adopt Pet' not found for user '" + userRole + "'");
    }
  }

  private void checkPetRecordsVisible(int size, int extra) {
    List<WebElement> list = listPets(petTable);
    assertEquals(size + extra, list.size(),
        "Number of pets records initially visible doesn't match.");
  }

  /**
   * Search pet by id
   * 
   * @param id pet id
   * @param extra extra number of returned records, for ex. headers and footers.
   * @return
   */
  private List<WebElement> searchPetById(int id, int extra) {
    searchInput.sendKeys("" + id);
    searchBtn.click();
    TestUtils.sleep(2);

    List<WebElement> list = listPets(petTable);

    // Expecting 1 element plus 2 extra rows
    assertEquals(1 + extra, list.size(), "Number of pets records after search doesn't match.");

    // Expecting pet-id in last element
    assertEquals(String.valueOf(id), list.get(1).findElement(By.className("pet-id")).getText(),
        "Test pet id doesn't match.");
    return list;
  }

  private List<WebElement> listPets(WebElement table) {
    return table.findElements(By.tagName("tr"));
  }

  private String acceptAlert() {
    // Wait b4 analyze alert
    TestUtils.sleep();

    Alert alert = driver.switchTo().alert();
    String text = alert.getText();
    alert.accept();

    return text;
  }

  public WebDriver getDriver() {
    return driver;
  }

  protected void deleteTestPetClose(int petId) {
    dbUtils.deleteTestPet(petId);
    getDriver().close();
  }

  // Database Utils wrapper

  protected int checkClientRef(int petId) {
    return dbUtils.checkClientRef(petId);
  }

  protected void checkPetAdopted(int petId) {
    dbUtils.checkPetAdopted(petId);
  }

  protected void deleteTestPet(int petId) {
    dbUtils.deleteTestPet(petId);
  }

  protected void delClientPetRef(int petId) {
    dbUtils.delClientPetRef(petId);
  }
}
