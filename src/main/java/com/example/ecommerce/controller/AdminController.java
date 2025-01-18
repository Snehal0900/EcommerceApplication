package com.example.ecommerce.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.entity.User;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

//    private final UserService userService;
//
//    public AdminController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping("/users")
//    public List<User> getAllUsers() {
//        return userService.findAllUsers();
//    }
//
//    @PostMapping("/assign-role")
//    public ResponseEntity<?> assignRole(@RequestParam String username, @RequestParam String role) {
//        userService.assignRole(username, role);
//        return ResponseEntity.ok("Role assigned successfully.");
//    }
}

