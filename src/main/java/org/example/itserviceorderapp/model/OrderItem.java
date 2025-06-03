package org.example.itserviceorderapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "order_items")
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private ITService service;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "item_price", nullable = false)
    private Double itemPrice;
}