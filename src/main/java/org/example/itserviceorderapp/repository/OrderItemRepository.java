package org.example.itserviceorderapp.repository;

import org.example.itserviceorderapp.model.OrderItem;
import org.example.itserviceorderapp.model.Order;
import org.example.itserviceorderapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Find all items for a specific order
    List<OrderItem> findByOrder(Order order);

    // Find all order items for a specific product
    List<OrderItem> findByProduct(Product product);

    // Optional: Find by order and product (if needed)
    List<OrderItem> findByOrderAndProduct(Order order, Product product);
}
