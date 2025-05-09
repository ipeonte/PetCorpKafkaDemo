package com.example.demo.rest.security.auth_server.dto;

public record UserInfo(String name, String[] roles, long mask) {
}
