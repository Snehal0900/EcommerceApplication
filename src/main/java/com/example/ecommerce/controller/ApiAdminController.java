package com.example.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dto.EditUserRequest;
import com.example.ecommerce.entity.Roles;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.RolesService;
import com.example.ecommerce.service.UserService;

@RestController
@RequestMapping("/api/admin")
public class ApiAdminController {

	@Autowired
    private UserService userService;
	
	@Autowired
    private RolesService rolesService;


    @Secured("ROLE_ADMIN")
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/roles")
    public List<Roles> getAllRoles(Model model) {
        return rolesService.getAllRoles();
    }
    
    @Secured("ROLE_ADMIN")
    @PostMapping("/user/{userId}/edit")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody EditUserRequest editUserRequest) {
        User user = userService.findUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"); 
        }

        userService.editUser(userId, editUserRequest.getUsername(), editUserRequest.getRole());  // Update user details
        return ResponseEntity.ok("New Details saved successfully"); 
    }
    
    @Secured("ROLE_ADMIN")
    @PostMapping("/createRole")
    public ResponseEntity<String> createRole(@RequestBody Roles role) {
        if (role.getName() == null || role.getName().isEmpty()) {
            return ResponseEntity.badRequest().body("Role is Empty");  
        }
        rolesService.createRole(role);
        return ResponseEntity.ok("Role created successfully");  
    }


    @Secured("ROLE_ADMIN")
    @DeleteMapping("/user/{userId}/delete")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }    
    
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/roles/{roleId}/delete")
    public ResponseEntity<String> deleteRole(@PathVariable long roleId, Model model) {
    	try {
    		rolesService.deleteRoleById(roleId);
    	}
        catch (DataIntegrityViolationException e) {
        	return ResponseEntity.status(HttpStatus.CONFLICT).body("Role is assigned to users and cannot be deleted.");
        }
       
    	return ResponseEntity.ok("Role deleted successfully");
    }
}
