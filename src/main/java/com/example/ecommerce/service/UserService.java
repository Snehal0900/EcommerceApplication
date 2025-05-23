package com.example.ecommerce.service;

import java.util.List;

import com.example.ecommerce.entity.Roles;
import com.example.ecommerce.entity.User;

public interface UserService {
	
	public void registerUser(String username, String password, String role);
	
	public User findUserById(Long userId);
	
	public User findUserByUsername(String username);
	
	public List<User> getAllUsers();
	
	public Roles getUserRole(Long userId);
	
	public void editUser(Long userId, String username, String role);
	
	public void deleteUser(Long userId);
	
	public long countUsers();
	
	public boolean usernameExists(String username);
}
