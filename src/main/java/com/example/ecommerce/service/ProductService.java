package com.example.ecommerce.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;

public interface ProductService {
	public void saveProduct(Product product);

	public void saveProductWithImage(Product product, MultipartFile image);
	
	public void updateProductWithImage(Long id, Product updatedProduct, MultipartFile image);
	
	public Product getProductById(Long id);
	
	public void deleteProduct(Long id);

	public List<Product> getProductsBySeller(User currentUser);
	
	public List<Product> getAllProducts();
	
	public List<Product> findByName(String name);
	
	public List<Product> findByMinPrice(Double minPrice);
	
	public List<Product> findByMaxPrice(Double maxPrice);
}
