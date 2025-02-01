package com.example.ecommerce.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.ProductRepository;

@Service
public class ProductImpl implements ProductService {
	
	@Value("${upload.dir}")
    private String uploadDir;
	
	
	@Autowired
    private ProductRepository productRepository;
	
	@Override
	public void saveProduct(Product product) {
		productRepository.save(product);
		
	}

	@Override
    public void saveProductWithImage(Product product, MultipartFile image) {
    	String fileName = image.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir);
        
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new RuntimeException("Could not create directory for images", e);
            }
        }

        // Save the image
        try {
            image.transferTo(uploadPath.resolve(fileName));
            product.setImagePath("/uploads/images/" + fileName); // Save image path
            System.out.println(fileName);
            
        } catch (IOException e) {
            throw new RuntimeException("Could not save the image file", e);
        }

        productRepository.save(product);
    }

	@Override
    public void updateProductWithImage(Long id, Product updatedProduct, MultipartFile image) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        // Update product details
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStock(updatedProduct.getStock());

        // Update image if a new one is provided
        if (image != null && !image.isEmpty()) {
        	String imagePath = image.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir); // Save to static/images folder

            if (!Files.exists(uploadPath)) {
                try {
                    Files.createDirectories(uploadPath);
                } catch (IOException e) {
                    throw new RuntimeException("Could not create directory for images", e);
                }
            }
            
         // Delete old image
            String oldImagePath = existingProduct.getImagePath();
            if (oldImagePath != null) {
                Path oldImageFile = Paths.get(uploadDir).resolve(Paths.get(oldImagePath).getFileName());
                try {
                    Files.deleteIfExists(oldImageFile);
                } catch (IOException e) {
                    throw new RuntimeException("Could not delete old image file", e);
                }
            }

            // Save the new image
            try {
                image.transferTo(uploadPath.resolve(imagePath));
                existingProduct.setImagePath("/uploads/images/" + imagePath); // Save the relative image path
            } catch (IOException e) {
                throw new RuntimeException("Could not save the image file", e);
            }
        }

        productRepository.save(existingProduct);
    }

	@Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

	@Override
    public void deleteProduct(Long id) {
    	 Product existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

    	String imagePath = existingProduct.getImagePath();
        if (imagePath != null) {
            Path imageFile = Paths.get(uploadDir).resolve(Paths.get(imagePath).getFileName());
            try {
                Files.deleteIfExists(imageFile);
            } 
            catch (IOException e) {
                throw new RuntimeException("Could not delete old image file", e);
            }
        }
        
        productRepository.deleteById(id);
    }

	@Override
	public List<Product> getProductsBySeller(User currentUser) {
		return productRepository.findByCreatedBy(currentUser);
	}

	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}
	
	@Override
	public List<Product> findByName(String name) {
	    return productRepository.findByNameContainingIgnoreCase(name);
	}

	@Override
	public List<Product> findByMinPrice(Double minPrice) {
	    return productRepository.findByPriceGreaterThanEqual(minPrice);
	}

	@Override
	public List<Product> findByMaxPrice(Double maxPrice) {
	    return productRepository.findByPriceLessThanEqual(maxPrice);
	}
}
