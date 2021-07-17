package com.example.demo.petcorp.ui.web.configuration;

import org.springframework.stereotype.Service;

import com.example.rest.common.security.service.UserInfo;
import com.example.rest.common.security.service.UserInfoService;

@Service
public class TestUserInfoService implements UserInfoService {

  private UserInfo userInfo;

  public TestUserInfoService() {
    userInfo = new UserInfo("Amonymous", "0");
  }

  @Override
  public UserInfo getUserInfo() {
    return userInfo;
  }

  public void setUserInfo(UserInfo userInfo) {
    this.userInfo = userInfo;
  }
}
