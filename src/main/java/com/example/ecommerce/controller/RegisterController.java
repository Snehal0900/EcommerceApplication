package com.example.ecommerce.controller;

import com.example.ecommerce.dto.RegisterRequest;
import com.example.ecommerce.entity.Roles;
import com.example.ecommerce.service.RolesService;
import com.example.ecommerce.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private RolesService rolesService;
    
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
    	List<Roles> roles = rolesService.getAllRoles();
    	model.addAttribute("roles", roles);
        return "register";  // This should map to the register.html page
    }
    
    
    @PostMapping(value = "/register")
    public String registerWithForm(@ModelAttribute RegisterRequest registerRequest, Model model) {
    	 System.out.println("Password " + registerRequest.getPassword());
        userService.registerUser(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getRole());
        model.addAttribute("message", "User registered successfully!");
        return "redirect:/auth/login"; // Redirect for Thymeleaf form submission
    }
}
