package com.ait.transporte.repository;

import com.ait.transporte.model.OrderAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderAssignmentRepository extends JpaRepository<OrderAssignment, UUID> {
    Optional<OrderAssignment> findByOrder_IdOrder(UUID orderId);
}
