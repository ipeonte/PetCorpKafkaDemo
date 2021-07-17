package com.example.demo.petcorp.ui.web.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.example.demo.petcorp.ui.web.SitTestConstants;
import com.example.demo.petcorp.ui.web.SitTestProperties;
import com.example.demo.petcorp.ui.web.TestUser;

@SpringBootTest
@TestPropertySource("classpath:/sit.properties")
public class PetStoreDemoSitUiTest extends AbstractPetStoreDemoUiTest {

  @Autowired
  private SitTestProperties props;

  @Override
  protected String preAuthUser(String userName) {
    // Do nothing
    return null;
  }

  @Override
  protected String postAuthUser(String userName) {
    TestUser user = SitTestConstants.USER_MASKS.get(userName);
    WebElement select = getDriver().findElement(By.id("username"));
    new Select(select).selectByVisibleText(user.getUserName());

    getDriver().findElement(By.tagName("button")).click();
    TestUtils.sleep();

    // Get mask
    return user.getMask();
  }

  @Override
  protected String getBaseUrl() {
    return props.getBaseUrl();
  }

}
