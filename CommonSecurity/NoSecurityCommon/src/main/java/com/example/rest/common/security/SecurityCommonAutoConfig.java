package com.example.rest.common.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * AutoConfiguration file for shared library
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
@Configuration
@EnableConfigurationProperties
@ComponentScan("com.example.rest.common.security")
public class SecurityCommonAutoConfig {

}
