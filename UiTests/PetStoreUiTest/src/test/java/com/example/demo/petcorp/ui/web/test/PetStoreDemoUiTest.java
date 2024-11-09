package com.example.demo.petcorp.ui.web.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;
import com.example.demo.petcorp.ui.web.TestConstants;
import com.example.demo.petcorp.ui.web.configuration.TestUserInfoService;
import com.example.rest.common.security.service.UserInfo;

@TestPropertySource("classpath:/qa.properties")
@EmbeddedKafka(topics = {"adopt-pet", "adopt-pet-info"}, partitions = 1)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PetStoreDemoUiTest extends AbstractPetStoreDemoUiTest {

  // Start URL
  private String url;

  @LocalServerPort
  private void setLocalPort(int port) {
    url = "http://localhost:" + port + "/";
  }

  @Autowired
  private TestUserInfoService userInfoService;

  @Override
  protected String preAuthUser(String userName) {
    String mask = TestConstants.USER_MASKS.get(userName);
    userInfoService.setUserInfo(new UserInfo(userName, mask));
    return mask;
  }

  @Override
  protected String postAuthUser(String userName) {
    // Do nothing
    return null;
  }

  @Override
  protected String getBaseUrl() {
    return url;
  }
}
