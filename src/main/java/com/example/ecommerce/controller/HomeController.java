package com.example.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.UserService;

@Controller
@RequestMapping("/home")
public class HomeController {
	
	@Autowired
    private UserService userService;

    @Secured({"ROLE_SELLER"})
    @GetMapping("/seller")
    public String getSellerHomePage(Model model, Authentication authentication) {
    	User currentUser = userService.findUserByUsername(authentication.getName());
        model.addAttribute("user", currentUser);
        
        return "sellerHome";  // Redirect to seller's homepage
    }
    
    @Secured({"ROLE_BUYER"})
    @GetMapping("/buyer")
    public String getBuyerHomePage(Model model, Authentication authentication) {
    	User currentUser = userService.findUserByUsername(authentication.getName());
        model.addAttribute("user", currentUser);
        
        return "buyerHome";  // Redirect to buyer's homepage
    }
}
