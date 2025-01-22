package com.example.ecommerce.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dto.LoginRequest;
import com.example.ecommerce.util.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
    private JwtUtil jwtUtil;
	
	@PostMapping(value = "/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        System.out.println("POST Login endpoint hit!!");
        
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails.getUsername());
            
            List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());


            // Set JWT token as a cookie
            Cookie cookie = new Cookie("authToken", token);  // Cookie name should be 'token'
            cookie.setHttpOnly(true);  // Make it HTTP-only for security
            cookie.setSecure(true);  // Set this to true if you're using HTTPS
            cookie.setPath("/");  // Make it available for the entire domain
            cookie.setMaxAge(60 * 60 * 24);  // Set the cookie expiry time (1 day)

            response.addCookie(cookie);  // Add the cookie to the response
         
            // Return token in response body as well
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("authToken", token);
            System.out.println("token " + token);
            responseMap.put("roles", roles);


            return ResponseEntity.ok(responseMap);
        } 
        catch (Exception e) {
            System.out.println("Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
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
