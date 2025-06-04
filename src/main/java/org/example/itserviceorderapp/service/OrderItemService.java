package org.example.itserviceorderapp.service;

import org.example.itserviceorderapp.model.OrderItem;
import org.example.itserviceorderapp.model.Order;
import org.example.itserviceorderapp.model.Product;
import org.example.itserviceorderapp.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    public Optional<OrderItem> getOrderItemById(Long id) {
        return orderItemRepository.findById(id);
    }

    public List<OrderItem> getOrderItemsByOrder(Order order) {
        return orderItemRepository.findByOrder(order);
    }

    public List<OrderItem> getOrderItemsByProduct(Product product) {
        return orderItemRepository.findByProduct(product);
    }

    public List<OrderItem> getOrderItemsByOrderAndProduct(Order order, Product product) {
        return orderItemRepository.findByOrderAndProduct(order, product);
    }

    public OrderItem createOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    public OrderItem updateOrderItem(Long id, OrderItem updatedOrderItem) {
        return orderItemRepository.findById(id)
                .map(existingOrderItem -> {
                    existingOrderItem.setOrder(updatedOrderItem.getOrder());
                    existingOrderItem.setProduct(updatedOrderItem.getProduct());
                    existingOrderItem.setQuantity(updatedOrderItem.getQuantity());
                    existingOrderItem.setPrice(updatedOrderItem.getPrice());
                    // Set other fields as necessary
                    return orderItemRepository.save(existingOrderItem);
                })
                .orElseThrow(() -> new IllegalArgumentException("OrderItem not found with id: " + id));
    }

    public void deleteOrderItem(Long id) {
        orderItemRepository.deleteById(id);
    }
}
