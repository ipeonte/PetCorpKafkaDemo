package com.example.demo.petstore.rest.dto;

import com.example.rest.common.security.service.UserInfo;

/**
 * User Info DTO
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
public class UserInfoDto {

  private String breedInfoUrl;

  private UserInfo userInfo;

  public UserInfoDto() {
  }

  public UserInfoDto(String breedInfoUrl, UserInfo userInfo) {
    this.breedInfoUrl = breedInfoUrl;
    this.userInfo = userInfo;
  }

  public String getBreedInfoUrl() {
    return breedInfoUrl;
  }

  public void setBreedInfoUrl(String breedInfoUrl) {
    this.breedInfoUrl = breedInfoUrl;
  }

  public UserInfo getUserInfo() {
    return userInfo;
  }

  public void setUserInfo(UserInfo userInfo) {
    this.userInfo = userInfo;
  }
}
