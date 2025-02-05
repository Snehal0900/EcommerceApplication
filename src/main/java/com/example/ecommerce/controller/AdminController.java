package com.example.ecommerce.controller;

import com.example.ecommerce.dto.EditUserRequest;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.Roles;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.RolesService;
import com.example.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RolesService rolesService;
    
    @Autowired
    private ProductService productService;

    @Secured("ROLE_ADMIN")
    @GetMapping("/dashboard")
    public String showAdminPage(Model model, Authentication authentication) {
    	String adminName = authentication.getName();

        // Get total counts
        long userCount = userService.countUsers();
        long roleCount = rolesService.countRoles();

        // Add attributes to the model
        model.addAttribute("adminName", adminName);
        model.addAttribute("userCount", userCount);
        model.addAttribute("roleCount", roleCount);
        
        return "admin";  // This should map to the admin.html page
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/users")
    public String getAllUsers(Model model, Authentication authentication) {
    	String adminName = authentication.getName();
    	
        List<User> users = userService.getAllUsers();
        
        model.addAttribute("users", users);
        model.addAttribute("adminName", adminName);
        
        return "viewUsers";  // This maps to view-users.html
    }

    
    @Secured("ROLE_ADMIN")
    @GetMapping("/user/{userId}/edit")
    public String editUser(@PathVariable Long userId, Model model, Authentication authentication) {
    	String adminName = authentication.getName();
    	
    	User user = userService.findUserById(userId);
    	List<Roles> roles = rolesService.getAllRoles();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("adminName", adminName);
        
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
		User user = userService.findUserById(userId);
		
		List<Product> product = productService.getProductsBySeller(user);
	    if (!product.isEmpty()) {
	        for (Product p : product) {
	            productService.deleteProduct(p.getId());
	        }
	    }
	    userService.deleteUser(userId); // Implement this method in your service
	    return "redirect:/admin/users";  // Redirect back to users list
	}
	
	
	@Secured("ROLE_ADMIN")
    @GetMapping("/createRolePage")
    public String showCreateRolePage(Model model, Authentication authentication) {
		String adminName = authentication.getName();
		model.addAttribute("adminName", adminName);
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
    public String getAllRoles(Model model, Authentication authentication) {
    	String adminName = authentication.getName();
        List<Roles> roles = rolesService.getAllRoles();
        
        model.addAttribute("roles", roles);
        model.addAttribute("adminName", adminName);
        return "viewRoles";  // This maps to view-users.html
    }
    
    
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/roles/{roleId}/delete")
    public String deleteRole(@PathVariable long roleId, RedirectAttributes redirectAttributes) {
    	try {
    		rolesService.deleteRoleById(roleId);
    		return "redirect:/admin/roles";
    	}
        catch (DataIntegrityViolationException e) {
        	redirectAttributes.addFlashAttribute("error", "Role cannot be deleted because it is assigned to users.");
        }
       
    	return "redirect:/admin/roles";
    }
}