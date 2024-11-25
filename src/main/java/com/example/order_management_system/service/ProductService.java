package com.example.order_management_system.service;

import com.example.order_management_system.exception.ResourceNotFoundException;
import com.example.order_management_system.model.Order;
import com.example.order_management_system.model.Product;
import com.example.order_management_system.repository.OrderRepository;
import com.example.order_management_system.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public ProductService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public Product createProduct(Product product) {
        
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (product.getDescription() == null || product.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Product description cannot be null or empty");
        }
        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be greater than 0");
        }
    
        return productRepository.save(product);
    }
    

    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        if (existingProduct.getName() == null || existingProduct.getName().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (existingProduct.getDescription() == null || existingProduct.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Product description cannot be null or empty");
        }
        if (existingProduct.getPrice() == null || existingProduct.getPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be greater than 0");
        }
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        return productRepository.save(existingProduct);
    }

    // Get an product by ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // public Boolean deleteProduct(Long productId) {
    //     Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    //     // Remove the products from the order
    //     if (product != null) {
    //         orderRepository.removeProductFromOrders(productId);
    //         productRepository.delete(product); 
    //         return true;  // Successfully deleted
    //     }
    //     return false; 
    // }

    public Boolean deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    
        if (product != null) {
            List<Order> ordersWithProduct = orderRepository.findOrdersByProductId(productId);
            orderRepository.removeProductFromOrders(productId);
            productRepository.delete(product); 
            //After deleting the product check all the empty orders and delete em too
            for (Order order : ordersWithProduct) {
                
                if (order.getOrderProducts().isEmpty()) {
                    orderRepository.delete(order);
                } 
            }
            return true;
        }
        return false;
    }
    
}
