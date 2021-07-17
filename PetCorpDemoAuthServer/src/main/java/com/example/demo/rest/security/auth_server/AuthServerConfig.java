package com.example.demo.rest.security.auth_server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Security Configuration
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
@Configuration
public class AuthServerConfig extends WebSecurityConfigurerAdapter {

  @Value("${cookie.domain}")
  String cdomain;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off

    // Disable all security headers and csrf for demo
    http.csrf().disable().headers().disable()
 
        .authorizeRequests().anyRequest().authenticated().and()
        
        // Disable logout redirect
        .logout().logoutSuccessUrl("/login").permitAll().and()
        
        // Using basic form login with custom login page
        .formLogin().loginPage("/login").permitAll()
        
        // Redirect back to cookie domain
        .defaultSuccessUrl("http://" + cdomain + "/");
    
    // @formatter:on
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // Define few users & roles

    // @formatter:off
		auth.inMemoryAuthentication()
		    .withUser("user").password("{noop}password").roles("USER_0").and()
				.withUser("pet_keeper").password("{noop}password").roles("PET_KEEPER_01", "USER_0").and()
				.withUser("manager").password("{noop}password").roles("MANAGER_10", "PET_KEEPER_01", "USER_0");

		// @formatter:on

  }

}
