package com.example.ecommerce.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.ecommerce.dto.LoginRequest;
import com.example.ecommerce.util.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping(value = "/login", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response, HttpServletRequest request) {
        System.out.println("POST Login endpoint hit!!");
        
        System.out.println("Content-Type: " + request.getContentType()); // Log the Content-Type header
        System.out.println("Request Body: " + loginRequest);
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails.getUsername());

            // Set JWT token as a cookie
            Cookie cookie = new Cookie("authToken", token);  // Cookie name should be 'token'
            cookie.setHttpOnly(true);  // Make it HTTP-only for security
            cookie.setSecure(true);  // Set this to true if you're using HTTPS
            cookie.setPath("/");  // Make it available for the entire domain
            cookie.setDomain("localhost");  // Make sure the domain is correctly set
            cookie.setMaxAge(60 * 60 * 24);  // Set the cookie expiry time (1 day)

            response.addCookie(cookie);  // Add the cookie to the response
            System.out.println("cookie = " + cookie);

            // Return token in response body as well
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("authToken", token);
            System.out.println("token " + token);

            return ResponseEntity.ok(responseMap);
        } catch (Exception e) {
            System.out.println("Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("error", "Invalid credentials"));
        }
    }

    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        // Clear the cookie
        Cookie cookie = new Cookie("authToken", null); // Setting the value to null
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Set to true if using HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(0); // Expire the cookie immediately
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out successfully");
    }
}

