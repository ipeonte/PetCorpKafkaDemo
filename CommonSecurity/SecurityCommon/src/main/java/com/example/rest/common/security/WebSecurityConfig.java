package com.example.rest.common.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Security configuration for each Rest Web Service that using this common security library.
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  public static final String API_VER = "/api/v1";

  // App pets url
  public static final String ADMIN_URL = "/admin";
  
  //App pets url
  public static final String MGR_URL = "/mgr";

  public static final String[] AUTH_ROLES =
      new String[] { "MANAGER_10", "PET_KEEPER_01" };

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //@formatter:off
	
		http.csrf().disable()
	
    ////////////////////////////////////////////////////////////////////////////
    // ADMIN section.
		// 
    // Common section for pet handler and manager
    .authorizeRequests().antMatchers(API_VER + ADMIN_URL + "/**").hasAnyRole(AUTH_ROLES)
		
    ////////////////////////////////////////////////////////////////////////////
    // MANAGER section.
    // 
    // Manager only section
    .and().authorizeRequests().antMatchers(API_VER + MGR_URL + "/**").hasRole("MANAGER_10")
    
		//////////////////////////////////////////////////////////////
		// 		Last Deny rule: Deny All -> Allow Authenticated only	//
		//////////////////////////////////////////////////////////////
		.and().authorizeRequests().anyRequest().authenticated();
	
		//@formatter:on
  }

}
