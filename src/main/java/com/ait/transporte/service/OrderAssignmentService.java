package com.ait.transporte.service;

import com.ait.transporte.dto.OrderAssignmentDTO;
import com.ait.transporte.model.AssignmentFile;
import com.ait.transporte.model.AssignmentFileType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface OrderAssignmentService {
    OrderAssignmentDTO assignDriver(UUID orderId, UUID driverId);

    OrderAssignmentDTO getAssignment(UUID orderId);

    AssignmentFile addFile(UUID orderId, AssignmentFileType type, MultipartFile file) throws IOException;

    AssignmentFile getFile(UUID orderId, AssignmentFileType type);
}
