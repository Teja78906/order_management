package com.example.order_management_system.controller;

import com.example.order_management_system.exception.ResourceNotFoundException;
import com.example.order_management_system.model.Product;
import com.example.order_management_system.service.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Create Product
    @PostMapping
    public ResponseEntity<Map<String, String>> createProduct(@RequestBody Product product) {
        Map<String, String> response = new HashMap<>();
        try {
            Product createdProduct = productService.createProduct(product);
            response.put("status", "success");
            response.put("message", "Product created successfully.");
            response.put("productId", createdProduct.getId().toString());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("status", "failure");
            response.put("message", e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Update Product
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Map<String, String> response = new HashMap<>();
        try {
            productService.updateProduct(id, product);
            response.put("status", "success");
            response.put("message", "Product updated successfully.");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("status", "failure");
            response.put("message", "Product not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("status", "failure");
            response.put("message", "An error occurred while updating the product.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Get Product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            response.put("status", "success");
            response.put("product", product.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "failure");
            response.put("message", "Product not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Get All Products
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        Map<String, Object> response = new HashMap<>();
        List<Product> products = productService.getAllProducts();
        if (!products.isEmpty()) {
            response.put("status", "success");
            response.put("products", products);
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "failure");
            response.put("message", "No products found.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            productService.deleteProduct(id);
            response.put("status", "success");
            response.put("message", "Product deleted successfully.");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("status", "failure");
            response.put("message", "Product not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("status", "failure");
            response.put("message", "An error occurred while deleting the product.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
