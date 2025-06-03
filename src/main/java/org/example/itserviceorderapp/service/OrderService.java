package org.example.itserviceorderapp.service;

import org.example.itserviceorderapp.model.Order;
import org.example.itserviceorderapp.model.Customer;
import org.example.itserviceorderapp.model.ITService;
import org.example.itserviceorderapp.model.OrderItem;
import org.example.itserviceorderapp.repository.OrderRepository;
import org.example.itserviceorderapp.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ServiceService serviceService;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public Order createOrder(Long customerId, List<Long> serviceIds, List<Integer> quantities) {
        Customer customer = customerService.getCustomerById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("Pending");
        order.setTotalAmount(0.0);

        Order savedOrder = orderRepository.save(order);

        Double totalAmount = 0.0;
        for (int i = 0; i < serviceIds.size(); i++) {
            Long serviceId = serviceIds.get(i);
            Integer quantity = quantities.get(i);

            ITService service = serviceService.getServiceById(serviceId)
                    .orElseThrow(() -> new RuntimeException("Service not found with ID: " + serviceId));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setService(service);
            orderItem.setQuantity(quantity);
            orderItem.setItemPrice(service.getPrice());
            orderItemRepository.save(orderItem);

            totalAmount += service.getPrice() * quantity;
        }

        savedOrder.setTotalAmount(totalAmount);
        return orderRepository.save(savedOrder);
    }

    public Order updateOrder(Long id, Order orderDetails) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));

        order.setCustomer(orderDetails.getCustomer());
        order.setOrderDate(orderDetails.getOrderDate());
        order.setTotalAmount(orderDetails.getTotalAmount());
        order.setStatus(orderDetails.getStatus());
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}