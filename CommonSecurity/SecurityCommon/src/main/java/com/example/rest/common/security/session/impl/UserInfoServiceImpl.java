package com.example.rest.common.security.session.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.rest.common.security.service.UserInfo;
import com.example.rest.common.security.service.UserInfoService;

/**
 * Implementation for UserInfo Service
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

  @Override
  public UserInfo getUserInfo() {
    Authentication auth =
        SecurityContextHolder.getContext().getAuthentication();

    if (auth == null)
      return null;

    // Insert user name was used from login from authentication object
    String name = auth.getName();
    String mask = "";

    for (GrantedAuthority authority : auth.getAuthorities()) {
      String role = authority.getAuthority();
      String msk = role.substring(role.lastIndexOf("_") + 1);

      for (int i = 0; i < msk.length(); i++) {
        if (mask.length() < i + 1)
          mask += "0";
        char c = msk.charAt(i);
        if (c == '1' && mask.charAt(i) != '1')
          mask = mask.length() < msk.length() ? '1' + mask
              : mask.substring(0, i) + '1' + mask.substring(i + 1);
      }
    }

    return new UserInfo(name, mask);
  }

}
