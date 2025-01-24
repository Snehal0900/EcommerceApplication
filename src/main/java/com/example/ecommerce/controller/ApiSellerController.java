package com.example.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/api/home/seller")
public class ApiSellerController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // View all products
    @Secured("ROLE_SELLER")
    @GetMapping("/view")
    public List<Product> viewProducts(Authentication authentication) {
        User currentUser = userService.findUserByUsername(authentication.getName());
        return productService.getProductsBySeller(currentUser);
    }

    // Create product with image
    @Secured("ROLE_SELLER")
    @PostMapping("/add")
    public ResponseEntity<String> createProduct(@RequestParam("product") String productJson,
                                                @RequestParam("image") MultipartFile image,
                                                Authentication authentication) {
        Product product = null;
        try {
            // Parse the product object from JSON
            product = new ObjectMapper().readValue(productJson, Product.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product data: " + e.getMessage());
        }

        User currentUser = userService.findUserByUsername(authentication.getName());
        product.setCreatedBy(currentUser);

        // Save image and product
        productService.saveProductWithImage(product, image);

        return ResponseEntity.ok("Product added successfully");
    }

    // Edit product with image
    @Secured("ROLE_SELLER")
    @PostMapping("/{id}/edit")
    public ResponseEntity<String> editProduct(@PathVariable Long id,
                                              @RequestParam("product") String productJson,
                                              @RequestParam("image") MultipartFile image,
                                              Authentication authentication) {
        Product updatedProduct = null;
        try {
            // Parse the product object from JSON
            updatedProduct = new ObjectMapper().readValue(productJson, Product.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product data: " + e.getMessage());
        }

        Product existingProduct = productService.getProductById(id);
        if (!existingProduct.getCreatedBy().getUsername().equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Seller is not authenticated");
        }

        // Update product and image
        productService.updateProductWithImage(id, updatedProduct, image);

        return ResponseEntity.ok("Product updated successfully");
    }

    // Delete product
    @Secured("ROLE_SELLER")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id, Authentication authentication) {
        Product product = productService.getProductById(id);
        if (!product.getCreatedBy().getUsername().equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Seller is not authenticated");
        }
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
