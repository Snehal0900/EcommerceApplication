package com.example.ecommerce.controller;

import com.example.ecommerce.entity.Roles;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.RolesService;
import com.example.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RolesService rolesService;

    @Secured("ROLE_ADMIN")
    @GetMapping("/dashboard")
    public String showAdminPage() {
        return "admin";  // This should map to the admin.html page
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "viewUsers";  // This maps to view-users.html
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/createRolePage")
    public String showCreateRolePage() {
        return "createRole";  // This maps to createRole.html
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/createRole")
    public String createRole(@RequestBody Roles roles) {
    	
        if (roles.getName() == null || roles.getName().isEmpty()) {
            System.out.println(roles.getName());
            throw new IllegalArgumentException("Role name cannot be empty");
        }
        rolesService.createRole(roles);
        return "redirect:/admin/dashboard";
    }

	@Secured("ROLE_ADMIN")
	@PostMapping("/user/{userId}/edit")
	public String editUser(@PathVariable Long userId, @RequestParam String username, @RequestParam String role) {
	    userService.editUser(userId, username, role); // Implement this method in your service
	    return "redirect:/admin/users";  // Redirect back to users list
	}
	
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/user/{userId}/delete")
	public String deleteUser(@PathVariable Long userId) {
	    userService.deleteUser(userId); // Implement this method in your service
	    return "redirect:/admin/users";  // Redirect back to users list
	}
}