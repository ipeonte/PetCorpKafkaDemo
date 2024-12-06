package com.example.rest.common.security.session.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.rest.common.security.UserProperties;
import com.example.rest.common.security.service.UserInfo;
import com.example.rest.common.security.service.UserInfoService;

/**
 * Implementation for UserInfo Service to emulate test user
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

  @Autowired
  private UserProperties props;

  @Override
  public UserInfo getUserInfo() {
    return new UserInfo(props.getName(), props.getMask());
  }

}
