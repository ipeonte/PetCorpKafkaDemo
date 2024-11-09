package com.example.demo.petstore.rest.dto;

import com.example.rest.common.security.service.UserInfo;

/**
 * User Info DTO
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
public record UserInfoDto(String breedInfoUrl, UserInfo userInfo) {

}
