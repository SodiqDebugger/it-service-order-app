package org.example.itserviceorderapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private ITService service;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "item_price", nullable = false)
    private Double itemPrice;
}
