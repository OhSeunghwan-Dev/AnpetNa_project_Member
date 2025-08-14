package com.anpetna.order.domain;

import com.anpetna.item.domain.ItemEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
    public class OrderEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "order_id")
        private Long orderId;

        @ManyToOne
        @JoinColumn(name = "order_itemEntity", nullable = false)
        private ItemEntity itemEntity;

        @Column(name="order_price", nullable = false)
        private int price;

        @Column(name="order_quantity", nullable = false)
        private int quantity;

        @ManyToOne
        @JoinColumn(name = "order_ordersEntity", nullable = false)
        private OrdersEntity ordersEntity;
    }

