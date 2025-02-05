package com.example.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.service.ProductService;

@Controller
@RequestMapping("home/buyer/")
public class BuyerController {

	@Autowired
    private ProductService productService;

    // View buyer homepage
	@Secured("ROLE_BUYER")
    @GetMapping("/view")
    public String showBuyerHomepage(Model model, Authentication authentication) {
        // Only allow authenticated buyers to access the homepage
        if (authentication == null || !authentication.getAuthorities().toString().contains("ROLE_BUYER")) {
            return "redirect:/auth/login"; // Redirect to login if not a buyer
        }

        String buyerName = authentication.getName();
        // Fetch all available products for the homepage (filter or paginate if needed)
        List<Product> products = productService.getAllProducts();

        // Add products to the model for Thymeleaf rendering
        model.addAttribute("products", products);
        model.addAttribute("buyerName", buyerName);

        return "buyerHome"; // Thymeleaf template for the homepage
    }
	
	@GetMapping("/search")
	public String searchProducts(@RequestParam String searchType, @RequestParam String searchValue, Model model) {

		List<Product> products;

		switch (searchType) {
	        case "name":
	            products = productService.findByName(searchValue);
	            break;
	        case "minPrice":
	            products = productService.findByMinPrice(Double.parseDouble(searchValue));
	            break;
	        case "maxPrice":
	            products = productService.findByMaxPrice(Double.parseDouble(searchValue));
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid search type");
	    }
	    
	    model.addAttribute("products", products);
	    
        return "buyerHome";
	}

}
