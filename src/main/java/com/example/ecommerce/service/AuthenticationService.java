package com.example.ecommerce.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
	private final PasswordEncoder passwordEncoder;

    public AuthenticationService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean verifyPassword(String enteredPassword, String storedEncodedPassword) {
    	System.out.println("Password Matches!!!");
        return passwordEncoder.matches(enteredPassword, storedEncodedPassword);
    }
}
