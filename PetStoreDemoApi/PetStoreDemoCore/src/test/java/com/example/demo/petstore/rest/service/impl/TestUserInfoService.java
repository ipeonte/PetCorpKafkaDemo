package com.example.demo.petstore.rest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import com.example.rest.common.security.service.UserInfo;
import com.example.rest.common.security.service.UserInfoService;
import jakarta.servlet.http.HttpSession;

@Service
@SessionScope
public class TestUserInfoService implements UserInfoService {

  @Autowired
  private HttpSession session;

  public TestUserInfoService() {}

  @Override
  public UserInfo getUserInfo() {
    return session.getAttribute("user_info") != null ? (UserInfo) session.getAttribute("user_info")
        : new UserInfo("Anonymous", "0");
  }
}
