package com.example.order_management_system.controller;

import com.example.order_management_system.exception.ResourceNotFoundException;
import com.example.order_management_system.model.Order;
import com.example.order_management_system.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    // Constructor-based injection
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create a new order (with list of product IDs)
    @PostMapping
    public ResponseEntity<Map<String, String>> createOrder(@RequestBody Map<Long, Integer> productQuantities) {
        Map<String, String> response = new HashMap<>();
        try {
            Order createdOrder = orderService.createOrder(productQuantities);
            response.put("status", "success");
            response.put("orderId", createdOrder.getId().toString());
            response.put("message", "Order created successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (ResourceNotFoundException e) {
            response.put("status", "failure");
            response.put("message", e.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("status", "failure");
            response.put("message", "An error occurred while creating the order.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Add products with quantities to an existing order
    @PostMapping("/{orderId}/products")
    public ResponseEntity<Map<String, String>> addProductsToOrder(@PathVariable Long orderId, @RequestBody Map<Long, Integer> productQuantities) {
        Map<String, String> response = new HashMap<>();
        try {
            orderService.addProductsToOrder(orderId, productQuantities);
            response.put("status", "success");
            response.put("message", "Products added to order successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ResourceNotFoundException e) {
            response.put("status", "failure");
            response.put("message", "Order or Product not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }catch (Exception e) {
            response.put("status", "failure");
            response.put("message", e.toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Update an order (with product IDs and quantities)
    @PutMapping("/{orderId}")
    public ResponseEntity<Map<String, String>> updateOrder(@PathVariable Long orderId, @RequestBody Map<Long, Integer> productQuantities) {
        Map<String, String> response = new HashMap<>();
        try {
            orderService.updateOrder(orderId, productQuantities);
            response.put("status", "success");
            response.put("message", "Order updated successfully.");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("status", "failure");
            response.put("message", "Order or Product not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("status", "failure");
            response.put("message", "An error occurred while updating the order.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    // Get an order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            return ResponseEntity.ok(order.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Get all orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        if (!orders.isEmpty()) {
            return ResponseEntity.ok(orders);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    // Remove a product from an order
    @DeleteMapping("/{orderId}/products/{productId}")
    public ResponseEntity<Map<String, String>> removeProductFromOrder(@PathVariable Long orderId, @PathVariable Long productId) {
        Map<String, String> response = new HashMap<>();
        try {
            orderService.removeProductFromOrder(orderId, productId);
            response.put("status", "success");
            response.put("message", "Product Deleted from order successfully.");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            response.put("status", "failure");
            response.put("message", "Order or Product not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("status", "failure");
            response.put("message", "An error occurred while removing the product from the order.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Remove an entire order
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Map<String, String>> removeOrder(@PathVariable Long orderId) {
        Map<String, String> response = new HashMap<>();
        boolean isDeleted = orderService.removeOrder(orderId);
        if (isDeleted) {
            response.put("status", "success");
            response.put("message", "Order deleted successfully.");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "failure");
            response.put("message", "Order not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
