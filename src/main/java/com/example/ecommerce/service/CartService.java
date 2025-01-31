package com.example.ecommerce.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.core.Authentication;

import com.example.ecommerce.entity.Cart;

public interface CartService {

	public void addToCart(Long productId, Authentication authentication, int quantity);
	
	public List<Cart> getCart(Authentication authentication);
	
	public void updateQuantity(Long productId, int quantity, Authentication authentication);
	
	public BigDecimal getTotalPrice(Authentication authentication);
	
	public void removeFromCart(Long productId, Authentication authentication);
}
