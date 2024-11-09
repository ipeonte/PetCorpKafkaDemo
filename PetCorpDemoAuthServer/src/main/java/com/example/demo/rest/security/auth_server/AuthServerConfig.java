package com.example.demo.rest.security.auth_server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security Configuration
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
@Configuration
public class AuthServerConfig {

  @Value("${cookie.domain}")
  String cdomain;

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // @formatter:off
    
    http
      // Disable all security headers and csrf for demo
      .csrf(csrf -> csrf.disable())
      .headers(headers -> headers.disable())
      
      // Authenticate all
      .authorizeHttpRequests((authz) ->
        authz.anyRequest().authenticated())
      
      // Disable logout redirect
      .logout(logout -> logout.logoutSuccessUrl("/login").permitAll())
      
      // Using basic form login with custom login page
      .formLogin(form -> form.loginPage("/login")
          // Redirect back to cookie domain
          .defaultSuccessUrl("http://" + cdomain + "/").permitAll());
    
    // @formatter:on

    return http.build();
  }

  @Bean
  InMemoryUserDetailsManager userDetailsService() {
    // @formatter:off
    @SuppressWarnings("deprecation")
    UserDetails user = User.withDefaultPasswordEncoder()
      .username("pet_keeper").password("{noop}password").roles("USER_0")
      .username("user").password("{noop}password").roles("PET_KEEPER_01", "USER_0")
      .username("manager").password("{noop}password").roles("MANAGER_10", "PET_KEEPER_01", "USER_0")
      .build();
    // @formatter:on
    return new InMemoryUserDetailsManager(user);
  }
}
