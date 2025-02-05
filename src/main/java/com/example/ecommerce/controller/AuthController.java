package com.example.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ecommerce.dto.LoginRequest;
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

    @PostMapping(value = "/login")
    public String login(@ModelAttribute LoginRequest loginRequest, HttpServletResponse response, RedirectAttributes redirectAttributes) {   
    	System.out.println("POST Login endpoint hit!!");
        
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails.getUsername());
            
            // Set JWT token as a cookie
            Cookie cookie = new Cookie("authToken", token);  // Cookie name should be 'token'
            cookie.setHttpOnly(true);  // Make it HTTP-only for security
            cookie.setSecure(true);  // Set this to true if you're using HTTPS
            cookie.setPath("/");  // Make it available for the entire domain
            cookie.setMaxAge(60 * 60 * 24);  // Set the cookie expiry time (1 day)

            response.addCookie(cookie);  // Add the cookie to the response
   
            
            boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
            boolean isSeller = userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_SELLER"));
            boolean isBuyer = userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_BUYER"));
            
            if (isAdmin) {
                return "redirect:/admin/dashboard"; // Redirect to admin dashboard for admins
            }
            else if (isSeller) {
            	return "redirect:/home/seller/products/view"; // Redirect to seller home page for sellers
            }
            else if(isBuyer){
            	return "redirect:/home/buyer/view"; // Redirect to buyer home page for buyers
            }
            
            return "redirect:/auth/login";
        } 
        catch (Exception e) {
        	redirectAttributes.addFlashAttribute("error", "Invalid credentials. Try again");
            return "redirect:/auth/login"; 
        }
    }

    
    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        // Clear the cookie
        Cookie cookie = new Cookie("authToken", null); // Setting the value to null
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Set to true if using HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(0); // Expire the cookie immediately
        response.addCookie(cookie);

        return "redirect:/auth/login";
    }
}

