package org.example.itserviceorderapp.repository;

import org.example.itserviceorderapp.model.Order;
import org.example.itserviceorderapp.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Find all orders for a specific customer
    List<Order> findByCustomer(Customer customer);

    // Find all orders by status
    List<Order> findByStatus(String status);

    // Optional: Find all orders placed after a specific date
    List<Order> findByOrderDateAfter(java.time.LocalDateTime date);

    // Optional: Find all orders with total amount greater than a value
    List<Order> findByTotalAmountGreaterThan(Double amount);
}
