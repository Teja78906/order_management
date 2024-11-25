package com.example.order_management_system.service;

import com.example.order_management_system.exception.ResourceNotFoundException;
import com.example.order_management_system.model.Order;
import com.example.order_management_system.model.OrderProduct;
import com.example.order_management_system.model.Product;
import com.example.order_management_system.repository.OrderProductRepository;
import com.example.order_management_system.repository.OrderRepository;
import com.example.order_management_system.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    @SuppressWarnings("unused")
    private final OrderProductRepository orderProductRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, OrderProductRepository orderProductRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderProductRepository = orderProductRepository;
    }

    public Order createOrder(Map<Long, Integer> productQuantities) {
        Order order = new Order();
        List<OrderProduct> orderProducts = productQuantities.entrySet().stream().map(entry -> {
            if(entry.getValue()<=0){
                throw new ResourceNotFoundException("Product quantity for product ID " + entry.getKey() + " must be greater than 0.");
            }
            Product product = productRepository.findById(entry.getKey()).orElseThrow();
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(entry.getValue());  // Set the quantity from the map
            return orderProduct;
        }).toList();
        order.setOrderProducts(orderProducts);
        return orderRepository.save(order);
    }
    

    // Get an order by ID
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    // Get all orders
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrder(Long orderId, Map<Long, Integer> productQuantities) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    
        // Clear existing products
        order.getOrderProducts().clear();
    
        // Add new products with their quantities
        for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();
            
            // Fetch the product by its ID
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product with ID " + productId + " not found"));
    
            // Create a new OrderProduct and set its properties
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(quantity);  // Set the quantity
    
            // Add the OrderProduct to the order
            order.getOrderProducts().add(orderProduct);
        }
    
        // Save and return the updated order
        return orderRepository.save(order);
    }
    

    public Order removeProductFromOrder(Long orderId, Long productId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        // Remove the product from the order
        boolean productRemoved = order.getOrderProducts().removeIf(orderProduct -> orderProduct.getProduct().getId().equals(productId));
        if (productRemoved && order.getOrderProducts().isEmpty()) {
            orderRepository.delete(order);
            return null;
        }
        return orderRepository.save(order);
    }

    public Boolean removeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        // Remove the products from the order
        if (order != null) {
            order.getOrderProducts().clear();
            orderRepository.delete(order); 
            return true;  // Successfully deleted
        }
        return false; 
        
    }

    public Order addProductsToOrder(Long orderId, Map<Long, Integer> productQuantities) {
        // Fetch the order by its ID
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        
        // Loop through the product IDs and quantities, adding them to the order
        for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();
            
            // Fetch the product by its ID
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product with ID " + productId + " not found"));
        
            // Check if the product already exists in the order
            boolean productExists = order.getOrderProducts().stream()
                    .anyMatch(orderProduct -> orderProduct.getProduct().getId().equals(productId));
        
            if (productExists) {
                // If the product already exists, update the quantity
                order.getOrderProducts().stream()
                        .filter(orderProduct -> orderProduct.getProduct().getId().equals(productId))
                        .forEach(orderProduct -> orderProduct.setQuantity(quantity));
            } else {
                // If the product is not already in the order, create a new OrderProduct and add it
                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setOrder(order);
                orderProduct.setProduct(product);
                orderProduct.setQuantity(quantity);  // Set the quantity
                order.getOrderProducts().add(orderProduct);
            }
        }
        
        // Save the updated order with the added products
        return orderRepository.save(order);
    }
    
    
}
