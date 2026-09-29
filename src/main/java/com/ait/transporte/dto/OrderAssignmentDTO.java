package com.ait.transporte.dto;

import com.ait.transporte.model.OrderAssignment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderAssignmentDTO(
        UUID id,
        UUID orderId,
        UUID driverId,
        String driverName,
        LocalDateTime assignedAt,
        List<AssignmentFileDTO> files
) {
    public static OrderAssignmentDTO fromEntity(
            OrderAssignment assignment,
            List<AssignmentFileDTO> files
    ) {
        return new OrderAssignmentDTO(
                assignment.getId(),
                assignment.getOrder().getIdOrder(),
                assignment.getDriver().getIdDriver(),
                assignment.getDriver().getName(),
                assignment.getAssignedAt(),
                files
        );
    }
}
