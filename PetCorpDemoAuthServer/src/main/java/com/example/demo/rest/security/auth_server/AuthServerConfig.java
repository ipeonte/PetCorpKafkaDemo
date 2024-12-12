package com.example.demo.rest.security.auth_server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import com.example.rest.common.security.core.CookieProperties;

/**
 * Security Configuration
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
@Configuration
public class AuthServerConfig {

  @Autowired
  private CookieProperties cookieProperties;

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // @formatter:off
    
    http
      // Disable all security headers and csrf for demo
      .csrf(csrf -> csrf.disable())
      .headers(headers -> headers.disable())
      
      // Authenticate all
      .authorizeHttpRequests((authz) ->
        authz
          // Allow login page
          .requestMatchers("/login.html").permitAll()
          .requestMatchers("/favicon.ico").permitAll()
          // Everything authenticated except login page
          .anyRequest().authenticated())
          
      
      // Disable logout redirect
      .logout(logout -> logout.logoutSuccessUrl("/login").permitAll())
      
      // Using basic form login with custom login page
      .formLogin(form -> form.loginPage("/login")
          // Redirect back to cookie domain
          .defaultSuccessUrl("http://" + cookieProperties.getDomain() + "/user_info").permitAll());
    
    // @formatter:on

    return http.build();
  }

  @Bean
  InMemoryUserDetailsManager userDetailsServiceEx() {
    // @formatter:off
    InMemoryUserDetailsManager mgr = new InMemoryUserDetailsManager();
    mgr.createUser(User.builder()
      .username("user").password("{noop}password").roles("USER_0").build());
    mgr.createUser(User.builder()
        .username("manager").password("{noop}password").roles("MANAGER_10", "PET_KEEPER_01", "USER_0").build());
    mgr.createUser(User.builder()
        .username("pet_keeper").password("{noop}password").roles("PET_KEEPER_01", "USER_0").build());

    // @formatter:on
    return mgr;
  }
}
