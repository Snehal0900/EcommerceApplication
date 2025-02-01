package com.example.ecommerce.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.core.Authentication;

import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.User;

public interface CartService {

	public void addToCart(Long productId, Authentication authentication, int quantity);
	
	public List<Cart> getCart(Authentication authentication);
	
	public void updateQuantity(Long productId, int quantity, Authentication authentication);
	
	public BigDecimal getTotalPrice(Authentication authentication);
	
	public void removeFromCart(Long productId, Authentication authentication);
	
	public void clearCart(User user);
	
	public void updateCart(Authentication authentication);
}
