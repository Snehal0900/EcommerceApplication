package com.example.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ecommerce.util.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;



@Controller
@RequestMapping("/auth")
public class AuthController {

	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";  // This should map to the login.html page
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, 
                                        @RequestParam String password, 
                                        HttpServletResponse response) {
    	
    	System.out.println("POST Login endpoint hit!!!");
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );

            // Get the user details
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Generate the JWT token
            String token = jwtUtil.generateToken(userDetails.getUsername());

            // Set token in HttpOnly cookie
            Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(true); // Makes the cookie inaccessible to JavaScript
            cookie.setSecure(true); // Use HTTPS (set to false if not using HTTPS)
            cookie.setPath("/"); // Cookie is valid for the entire application
            cookie.setMaxAge(60 * 60); // Set expiration time (1 hour for example)
            response.addCookie(cookie);

            return ResponseEntity.ok("Login successful");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("Invalid credentials");
        }
    }


}

