package com.example.githubproxy.infrastructure;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Username not found");
    }
}
