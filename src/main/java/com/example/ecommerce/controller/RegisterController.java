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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String registerWithForm(@ModelAttribute RegisterRequest registerRequest, RedirectAttributes redirectAttributes) {
    	if (userService.usernameExists(registerRequest.getUsername())) {
        	redirectAttributes.addFlashAttribute("error", registerRequest.getUsername() + " already exists. Try again with another username");
        	return "redirect:/register";
        }
        else {
        	userService.registerUser(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getRole());
        	return "redirect:/auth/login";
        }
    }
}
