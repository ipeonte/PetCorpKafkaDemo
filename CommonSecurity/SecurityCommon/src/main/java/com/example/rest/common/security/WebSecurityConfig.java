package com.example.rest.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for each Rest Web Service that using this common security library.
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  public static final String API_VER = "/api/v1";

  // App pets url
  public static final String ADMIN_URL = "/admin";

  // App pets url
  public static final String MGR_URL = "/mgr";

  // Manager role to allow changes
  public static final String MGR_10 = "MANAGER_10";

  // All authentication roles
  public static final String[] AUTH_ROLES = new String[] {MGR_10, "PET_KEEPER_01"};


  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // @formatter:off
    http.csrf(csrf -> csrf.disable())
      .authorizeHttpRequests((authz) ->
        ////////////////////////////////////////////////////////////////////////////
        // ADMIN section.
        //
        // Common section for pet handler and manager
        authz.requestMatchers(API_VER + ADMIN_URL + "/**").hasAnyRole(AUTH_ROLES)

        ////////////////////////////////////////////////////////////////////////////
        // MANAGER section.
        //
        // Manager only section
        .requestMatchers(API_VER + MGR_URL + "/**").hasRole(MGR_10)

        .anyRequest().authenticated());
    // @formatter:on

    return http.build();
  }
}
