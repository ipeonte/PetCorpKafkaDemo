package com.example.demo.rest.security.auth_server.controller;

import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.rest.security.auth_server.dto.UserInfo;
import com.example.demo.rest.security.auth_server.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class UserInfoController {

  @GetMapping("/user_info")
  public UserInfo showLoginInfo(Authentication authentication, HttpServletRequest req) {
    if (authentication == null)
      return null;

    String[] masks = authentication.getAuthorities().stream().map(GrantedAuthority::toString)
        .collect(Collectors.toList()).toArray(size -> new String[size]);
    return new UserInfo(authentication.getName(), masks, AuthUtils.calcUserMask(masks));
  }
}
