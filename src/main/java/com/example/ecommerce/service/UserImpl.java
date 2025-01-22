package com.example.ecommerce.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.ecommerce.entity.Roles;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.RolesRepository;
import com.example.ecommerce.repository.UserRepository;

@Service
public class UserImpl implements UserService {
	
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private RolesRepository rolesRepository;
	
	@Override
	public void registerUser(String username, String password, String role) {
		
		User user = new User();
        user.setUsername(username);
        
		String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        
        
        Roles userRole = rolesRepository.findByName(role).orElseThrow(() -> new IllegalArgumentException("Role not found"));
        Set<Roles> roles = new HashSet<>();
        roles.add(userRole);
        
        user.setRoles(roles);
        
        userRepository.save(user);
	}
	
	public User findUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
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
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setUsername(username);

        // Fetch the role from the database
        Roles newRole = rolesRepository.findByName(role).orElseThrow(() -> new IllegalArgumentException("Role not found"));

        user.setRoles(new HashSet<>(Collections.singleton(newRole)));

        userRepository.save(user);
    }

	@Override
	public void deleteUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
		
		user.getRoles().clear();
		userRepository.save(user);
	    
		userRepository.deleteById(userId);
	}

}
