
package com.anpetna.order.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id", nullable = false)
    private Long ordersId;

    @Column(name="orders_memberId", nullable = false)
    private String memberId;

    @Column(name="orders_cardId", nullable = false)
    private String cardId;

    @Column(name="orders_totalAmount", nullable = false)
    private int totalAmount;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<OrderEntity> orderItems;
}
