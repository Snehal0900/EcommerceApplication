package com.example.ecommerce.controller;

import com.example.ecommerce.entity.User;
import com.example.ecommerce.entity.Roles;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Set;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";  // This should map to the register.html page
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String role,
                               Model model) {
        System.out.println("POST Register endpoint hit!!");
        try {
            // Create a new user object
            User user = new User();
            user.setUsername(username);

            // Encode the password
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);

            // Assign the appropriate role (SELLER or BUYER)
            Roles userRole = rolesRepository.findByName(role)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found"));
            Set<Roles> roles = new HashSet<>();
            roles.add(userRole);

            user.setRoles(roles);

            // Save the user to the database
            userRepository.save(user);

            model.addAttribute("message", "User registered successfully with role: " + role);
            
            System.out.println(user.getUsername() + " registered successfully!!!");
            return "redirect:/auth/login"; // Redirect to login after successful registration
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            System.out.println(e.getMessage());
            return "register"; // Redirect back to registration if an error occurs
        }
    }

}
