package com.ait.transporte.controller;

import com.ait.transporte.dto.AssignmentFileDTO;
import com.ait.transporte.dto.CreateAssignmentRequest;
import com.ait.transporte.dto.OrderAssignmentDTO;
import com.ait.transporte.model.AssignmentFile;
import com.ait.transporte.model.AssignmentFileType;
import com.ait.transporte.service.OrderAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequestMapping("/v1/orders/{orderId}/assignment")
@RequiredArgsConstructor
public class OrderAssignmentController {

    private final OrderAssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<OrderAssignmentDTO> assignDriver(
            @PathVariable UUID orderId,
            @RequestBody CreateAssignmentRequest request
    ) {
        if (request == null || request.driverId() == null) {
            throw new IllegalArgumentException("El ID del conductor es obligatorio");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assignmentService.assignDriver(orderId, request.driverId()));
    }

    @GetMapping
    public ResponseEntity<OrderAssignmentDTO> getAssignment(@PathVariable UUID orderId) {
        return ResponseEntity.ok(assignmentService.getAssignment(orderId));
    }

    @PostMapping(value = "/document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AssignmentFileDTO> uploadDocument(
            @PathVariable UUID orderId,
            @RequestPart("file") MultipartFile file
    ) throws java.io.IOException {
        return ResponseEntity.ok(AssignmentFileDTO.fromEntity(
                assignmentService.addFile(orderId, AssignmentFileType.DOCUMENT, file)));
    }

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AssignmentFileDTO> uploadImage(
            @PathVariable UUID orderId,
            @RequestPart("file") MultipartFile file
    ) throws java.io.IOException {
        return ResponseEntity.ok(AssignmentFileDTO.fromEntity(
                assignmentService.addFile(orderId, AssignmentFileType.IMAGE, file)));
    }

    @GetMapping("/files/{type}")
    public ResponseEntity<ByteArrayResource> downloadFile(
            @PathVariable UUID orderId,
            @PathVariable AssignmentFileType type
    ) {
        AssignmentFile file = assignmentService.getFile(orderId, type);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(file.getFileName(), StandardCharsets.UTF_8)
                        .build()
                        .toString())
                .body(new ByteArrayResource(file.getContent()));
    }
}
