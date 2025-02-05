package com.example.ecommerce.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.service.CartService;

@Controller
@RequestMapping("/home/buyer/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    
    // View cart
    @Secured("ROLE_BUYER")
    @GetMapping("/view")
    public String viewCart(Model model, Authentication authentication) {
    	String buyerName = authentication.getName();
        List<Cart> cartItems = cartService.getCart(authentication);
        BigDecimal totalPrice = cartService.getTotalPrice(authentication);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("buyerName", buyerName);
        
        return "viewCart"; // Thymeleaf template to display cart
    }

    // Add product to cart
    @Secured("ROLE_BUYER")
    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Long productId, Authentication authentication, @RequestParam int quantity, RedirectAttributes redirectAttributes) {
    	try {
            cartService.addToCart(productId, authentication, quantity);
            redirectAttributes.addFlashAttribute("success", "Product added to cart.");
        } 
    	catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/home/buyer/cart/view";
    }
    
    @Secured("ROLE_BUYER")
    @PostMapping("/update/{productId}")
    public String updateQuantity(@PathVariable Long productId, @RequestParam int quantity, Authentication authentication, RedirectAttributes redirectAttributes) {
    	try {
    		cartService.updateQuantity(productId, quantity, authentication);
            redirectAttributes.addFlashAttribute("success", "Product added to cart.");
        } 
    	catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
    	
        return "redirect:/home/buyer/cart/view";
    }

    // Remove product from cart
    @Secured("ROLE_BUYER")
    @DeleteMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId, Authentication authentication) {
        cartService.removeFromCart(productId, authentication);
        
        return "redirect:/home/buyer/cart/view";
    }
}
