package com.example.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.UserService;

@Controller
@RequestMapping("/seller/products")
public class SellerController {

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	// View all products
	@Secured("ROLE_SELLER")
	@GetMapping("/view")
	public String viewProducts(Model model, Authentication authentication) {
		User currentUser = userService.findUserByUsername(authentication.getName());
		List<Product> products = productService.getProductsBySeller(currentUser);
		model.addAttribute("products", products);
		return "viewSellerProducts"; // Thymeleaf template for viewing products
	}

	// Show product creation form
	@Secured("ROLE_SELLER")
	@GetMapping("/add")
	public String showCreateProductForm(Model model) {
		model.addAttribute("product", new Product());
		return "addProducts"; // Thymeleaf template for creating a product
	}

	// Create product
	@Secured("ROLE_SELLER")
	@PostMapping("/add")
    public String createProduct(@ModelAttribute Product product, @RequestParam("image") MultipartFile image, Authentication authentication) {
        User currentUser = userService.findUserByUsername(authentication.getName());
        product.setCreatedBy(currentUser);

        // Save image and product
        productService.saveProductWithImage(product, image);

        return "redirect:/seller/products/view";
    }

	// Show edit form
	@Secured("ROLE_SELLER")
	@GetMapping("/{id}/edit")
	public String showEditProductForm(@PathVariable Long id, Model model, Authentication authentication) {
		Product product = productService.getProductById(id);
		if (!product.getCreatedBy().getUsername().equals(authentication.getName())) {
			return "redirect:/seller/products/view"; // Redirect if unauthorized
		}
		model.addAttribute("product", product);
		return "editProducts"; // Thymeleaf template for editing a product
	}
	
	// Edit product
	@Secured("ROLE_SELLER")
	@PostMapping("/{id}/edit")
    public String editProduct(@PathVariable Long id, @ModelAttribute Product updatedProduct, @RequestParam(value = "image", required = false) MultipartFile image, Authentication authentication) {
        Product existingProduct = productService.getProductById(id);
        if (!existingProduct.getCreatedBy().getUsername().equals(authentication.getName())) {
            return "redirect:/seller/products/view"; // Redirect if unauthorized
        }

        // Update product and image
        productService.updateProductWithImage(id, updatedProduct, image);

        return "redirect:/seller/products/view";
    }

	// Delete product
	@Secured("ROLE_SELLER")
	@DeleteMapping("/{id}/delete")
	public String deleteProduct(@PathVariable Long id, Authentication authentication) {
		Product product = productService.getProductById(id);
		if (!product.getCreatedBy().getUsername().equals(authentication.getName())) {
			return "redirect:/seller/products/view"; // Redirect if unauthorized
		}
		productService.deleteProduct(id);
		return "redirect:/seller/products/view"; // Redirect to product list
	}

}
