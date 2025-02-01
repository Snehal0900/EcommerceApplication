package com.example.ecommerce.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.ecommerce.service.CartService;

@Controller
@RequestMapping("/home/buyer/")
public class PaymentController {

    @Autowired
    private CartService cartService;

    @Secured("ROLE_BUYER")
    @GetMapping("/checkout")
    public String checkout(Model model, Authentication authentication) {
        BigDecimal totalPrice = cartService.getTotalPrice(authentication); // Calculate total price
        
        cartService.updateCart(authentication);

        model.addAttribute("totalPrice", totalPrice);
        
        return "checkout"; // Thymeleaf view for the checkout page
    }
    
    @Secured("ROLE_BUYER")
    @PostMapping("/success")
    public String success(Authentication authentication) {
        
        return "success"; // Thymeleaf view for the checkout page
    }
}
