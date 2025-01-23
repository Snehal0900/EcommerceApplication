package com.example.ecommerce.controller;

import com.example.ecommerce.dto.EditUserRequest;
import com.example.ecommerce.entity.Roles;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.RolesService;
import com.example.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    @GetMapping("/user/{userId}/edit")
    public String editUser(@PathVariable Long userId, Model model) {
    	User user = userService.findUserById(userId);
    	List<Roles> roles = rolesService.getAllRoles();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "editUser"; // Map to edit-user.html
    }

	@Secured("ROLE_ADMIN")
	@PostMapping("/user/{userId}/edit")
	public String editUser(@PathVariable Long userId, @ModelAttribute EditUserRequest editUserRequest) {
	    userService.editUser(userId, editUserRequest.getUsername(), editUserRequest.getRole());
	    return "redirect:/admin/users";  // Redirect back to users list
	}
	
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/user/{userId}/delete")
	public String deleteUser(@PathVariable Long userId) {
	    userService.deleteUser(userId); // Implement this method in your service
	    return "redirect:/admin/users";  // Redirect back to users list
	}
	
	
	@Secured("ROLE_ADMIN")
    @GetMapping("/createRolePage")
    public String showCreateRolePage() {
        return "createRole";  // This maps to createRole.html
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/createRole")
    public String createRole(@RequestParam String roleName) {
    	
    	if (roleName == null || roleName.isEmpty())  {
            throw new IllegalArgumentException("Role name cannot be empty");
        }
    	
    	Roles role = new Roles();
        role.setName(roleName);
        rolesService.createRole(role);
        return "redirect:/admin/dashboard";
    }
    
    @Secured("ROLE_ADMIN")
    @GetMapping("/roles")
    public String getAllRoles(Model model) {
        List<Roles> roles = rolesService.getAllRoles();
        model.addAttribute("roles", roles);
        return "viewRoles";  // This maps to view-users.html
    }
    
    
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/roles/{roleId}/delete")
    public String deleteRole(@PathVariable long roleId, Model model) {
    	try {
    		rolesService.deleteRoleById(roleId);
    	}
        catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "Role cannot be deleted because it is assigned to users.");
        }
       
    	return "redirect:/admin/roles";
    }
}