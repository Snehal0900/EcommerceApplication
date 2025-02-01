package com.example.ecommerce.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.CartRepository;

@Service
public class CartImpl implements CartService{
	
	@Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserService userService;
    
    // Get cart items
 	public List<Cart> getCart(Authentication authentication) {
         String username = authentication.getName();
         User user = userService.findUserByUsername(username);
         return cartRepository.findByUser(user);
     }
    
    // Add product to cart
	public void addToCart(Long productId, Authentication authentication, int quantity) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        String username = authentication.getName();
        User user = userService.findUserByUsername(username); 

        Product product = productService.getProductById(productId);
        
        if (product == null) {
            throw new IllegalArgumentException("Product not found.");
        }
        
        if (quantity > product.getStock()) {
            throw new IllegalArgumentException("Not enough stock available.");
        }

        Cart existingCartItem = cartRepository.findByUserAndProductId(user, productId);
        if (existingCartItem != null) {
            int newQuantity = existingCartItem.getQuantity() + quantity;

            if (newQuantity > product.getStock()) {
                throw new IllegalArgumentException("Cannot exceed available stock.");
            }

            existingCartItem.setQuantity(newQuantity);
            cartRepository.save(existingCartItem);
        } 
        else {
            Cart newCartItem = new Cart();
            newCartItem.setUser(user);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            cartRepository.save(newCartItem);
        }
    }

    // Remove product from cart
	public void removeFromCart(Long productId, Authentication authentication) {
		String username = authentication.getName();
	    User user = userService.findUserByUsername(username);

	    Cart cartItem = cartRepository.findByUserAndProductId(user, productId);

	    cartRepository.delete(cartItem);
    }
	
	public void updateQuantity(Long productId, int quantity, Authentication authentication) {
	    String username = authentication.getName();
	    User user = userService.findUserByUsername(username);

	    Product product = productService.getProductById(productId);
	    
	    if (product == null) {
            throw new IllegalArgumentException("Product not found.");
        }
        
        if (quantity > product.getStock()) {
            throw new IllegalArgumentException("Not enough stock available.");
        }
	    
        Cart cartItem = cartRepository.findByUserAndProductId(user, productId);
	    if (cartItem != null) {
	    	cartItem.setQuantity(quantity);
	        cartRepository.save(cartItem); // Save updated quantity
	    }
	}

	public BigDecimal getTotalPrice(Authentication authentication) {
	    List<Cart> cartItems = getCart(authentication);
	    return cartItems.stream()
	            .map(cartItem -> cartItem.getProduct().getPrice()
	                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()))) // Multiply price * quantity
	            .reduce(BigDecimal.ZERO, BigDecimal::add); // Sum all prices
	}
	
	public void clearCart(User user) {
	    List<Cart> cartItems = cartRepository.findByUser(user);
	    cartRepository.deleteAll(cartItems);
	}
	
	public void updateCart(Authentication authentication) {
		User user = userService.findUserByUsername(authentication.getName());
	    List<Cart> cartItems = cartRepository.findByUser(user);
	    
	    for (Cart cartItem : cartItems) {
	        Product product = cartItem.getProduct();

	        if (product.getStock() < cartItem.getQuantity()) {
	        	throw new IllegalArgumentException("Not enough stock for " + product.getName());
	        }

	        product.setStock(product.getStock() - cartItem.getQuantity());
	        productService.saveProduct(product);
	    }

        // Clear the cart after successful "purchase"
        clearCart(user);     
	}
}

