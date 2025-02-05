package com.example.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{
	
	List<Cart> findByUser(User user);
	
	Cart findByUserAndProductId(User user, Long productId);

	List<Cart> findByProductId(Long productId);
}
