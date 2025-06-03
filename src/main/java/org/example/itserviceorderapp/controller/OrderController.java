package org.example.itserviceorderapp.controller;

import org.example.itserviceorderapp.model.Order;
import org.example.itserviceorderapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Yangi buyurtma yaratish uchun maxsus endpoint
    // POST /api/orders/create
    // Request Body: { "customerId": 1, "serviceIds": [1, 2], "quantities": [1, 1] }
    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody OrderCreationRequest request) {
        try {
            Order newOrder = orderService.createOrder(
                    request.getCustomerId(),
                    request.getServiceIds(),
                    request.getQuantities()
            );
            return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Xato xabarini qaytarish ham mumkin
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
        try {
            Order updatedOrder = orderService.updateOrder(id, orderDetails);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public static class OrderCreationRequest {
        private Long customerId;
        private List<Long> serviceIds;
        private List<Integer> quantities;

        public Long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Long customerId) {
            this.customerId = customerId;
        }

        public List<Long> getServiceIds() {
            return serviceIds;
        }

        public void setServiceIds(List<Long> serviceIds) {
            this.serviceIds = serviceIds;
        }

        public List<Integer> getQuantities() {
            return quantities;
        }

        public void setQuantities(List<Integer> quantities) {
            this.quantities = quantities;
        }
    }
}