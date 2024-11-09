package com.example.demo.rest.security.auth_server.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.rest.common.security.core.Constants;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class UserInfoController {

  @GetMapping("/user_info")
  public String showLoginInfo(Authentication authentication, HttpServletRequest req) {
    // Insert user name was used from login from standard http session
    String message = Constants.USER_NAME_KEY + ":"
        + req.getSession(false).getAttribute(Constants.USER_NAME_KEY).toString() + "|";

    if (authentication != null) {
      // Insert user name was used from login from authentication object
      message += "authenticationName:" + authentication.getName() + "|roles:";
      String roles = "";
      for (GrantedAuthority authority : authentication.getAuthorities())
        roles += "," + authority.getAuthority();

      message += roles.substring(1);
    }

    return message + "|sessionId:" + req.getSession(false).getId();
  }
}
