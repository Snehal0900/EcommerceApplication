package com.example.ecommerce.service;

import java.util.List;

import com.example.ecommerce.entity.Roles;
import com.example.ecommerce.entity.User;

public interface UserService {
	
	public void registerUser(User user);
	
	public List<User> getAllUsers();
	
	public Roles getUserRole(Long userId);
	
	public void editUser(Long userId, String username, String role);
	
	public void deleteUser(Long userId);
}
