package com.ait.transporte.dto;

import com.ait.transporte.model.Order;
import com.ait.transporte.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private UUID idOrder;
    private OrderStatus status;
    private String origin;
    private String destination;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static OrderDTO fromEntity(Order order) {
        return new OrderDTO(
                order.getIdOrder(),
                order.getStatus(),
                order.getOrigin(),
                order.getDestination(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    public Order toEntity() {
        return new Order(idOrder, status, origin, destination, createdAt, updatedAt);
    }
}
