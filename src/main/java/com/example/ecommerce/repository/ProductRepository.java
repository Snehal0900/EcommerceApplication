package com.example.ecommerce.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCreatedBy(User seller);
    
    List<Product> findByCreatedById(Long sellerId);

	List<Product> findByNameContainingIgnoreCase(String name);

	List<Product> findByPriceGreaterThanEqual(Double minPrice);

	List<Product> findByPriceLessThanEqual(Double maxPrice);
    
}
