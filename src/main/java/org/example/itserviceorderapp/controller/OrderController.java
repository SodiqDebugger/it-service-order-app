package org.example.itserviceorderapp.controller;

import org.example.itserviceorderapp.model.Order;
import org.example.itserviceorderapp.service.OrderService;
import org.example.itserviceorderapp.exception.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * REST controller for managing orders.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Get all orders.
     */
    @GetMapping
    public List<Order> getAllOrders() {
        logger.info("Fetching all orders");
        return orderService.getAllOrders();
    }

    /**
     * Get order by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        logger.info("Fetching order with ID: {}", id);
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    /**
     * Create a new order.
     */
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderCreationRequest request) {
        logger.info("Creating new order for customer ID: {}", request.getCustomerId());
        Order newOrder = orderService.createOrder(
                request.getCustomerId(),
                request.getServiceIds(),
                request.getQuantities()
        );
        return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
    }

    /**
     * Update an existing order.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @Valid @RequestBody Order orderDetails) {
        logger.info("Updating order with ID: {}", id);
        try {
            Order updatedOrder = orderService.updateOrder(id, orderDetails);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            throw new OrderNotFoundException(id);
        }
    }

    /**
     * Delete an order by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        logger.info("Deleting order with ID: {}", id);
        if (orderService.getOrderById(id).isPresent()) {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new OrderNotFoundException(id);
        }
    }

    /**
     * Request body for creating an order.
     */
    public static class OrderCreationRequest {
        @NotNull
        private Long customerId;
        @NotEmpty
        private List<Long> serviceIds;
        @NotEmpty
        private List<Integer> quantities;

        public Long getCustomerId() { return customerId; }
        public void setCustomerId(Long customerId) { this.customerId = customerId; }

        public List<Long> getServiceIds() { return serviceIds; }
        public void setServiceIds(List<Long> serviceIds) { this.serviceIds = serviceIds; }

        public List<Integer> getQuantities() { return quantities; }
        public void setQuantities(List<Integer> quantities) { this.quantities = quantities; }
    }
}
