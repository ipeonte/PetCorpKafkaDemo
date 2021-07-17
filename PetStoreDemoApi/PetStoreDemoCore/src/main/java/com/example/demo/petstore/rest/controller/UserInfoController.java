package com.example.demo.petstore.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.petcorp.shared.SharedConstants;
import com.example.demo.petstore.rest.dto.UserInfoDto;
import com.example.rest.common.security.service.UserInfoService;

/**
 * PetStore Controller
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
@RestController
@RequestMapping(SharedConstants.BASE_URL)
public class UserInfoController {

  @Autowired
  private UserInfoService userInfoService;

  // Site with breed information
  @Value("${breed-info-url}")
  private String breedInfoUrl;

  @RequestMapping("/user_info")
  public UserInfoDto getUserInfo() {
    return new UserInfoDto(breedInfoUrl, userInfoService.getUserInfo());
  }
}
