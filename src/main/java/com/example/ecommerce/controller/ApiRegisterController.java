package com.example.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dto.RegisterRequest;
import com.example.ecommerce.service.UserService;

@RestController
@RequestMapping("/api")
public class ApiRegisterController {
	
	 @Autowired
	 private UserService userService;
	
	@PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> registerWithApi(@RequestBody RegisterRequest registerRequest) {
		System.out.println("Post Register endpoint hit!!");
		try {
			userService.registerUser(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getRole());
		    return ResponseEntity.ok("User registered successfully via API!");
		}
		catch (Exception e) {
	        System.out.println("Invalid credentials");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
	    }
	}
}
