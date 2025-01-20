package com.example.ecommerce.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ecommerce.entity.Roles;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.UserRepository;

@Service
public class UserImpl implements UserService {
	
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
    private UserRepository userRepository;
	
	@Override
	public void registerUser(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
	}
	
	public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get a user's role by userId
    public Roles getUserRole(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getRoles().stream().findFirst().orElseThrow(() -> new RuntimeException("No role found for the user"));
    }

	@Override
	public void editUser(Long userId, String username, String role) {
		User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setUsername(username);
        
        // Set role based on role passed (assuming 'role' is a string name)
        Roles userRole = new Roles();
        userRole.setName(role);  // Here you should load the actual Role from the database if it's not just a string

        user.setRoles(Set.of(userRole)); // Assuming user has a single role

        userRepository.save(user);
	}

	@Override
	public void deleteUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
		userRepository.delete(user);
	}

}
