package com.ait.transporte.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "`order`")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID idOrder;

    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name="origin", nullable = false)
    private String origin;

    @Column(name="destination", nullable = false)
    private String destination;

    @Column(name="createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name="updatedAt", nullable = false)
    private LocalDateTime updatedAt;


}
