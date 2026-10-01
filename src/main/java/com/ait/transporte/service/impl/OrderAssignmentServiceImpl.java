package com.ait.transporte.service.impl;

import com.ait.transporte.dto.AssignmentFileDTO;
import com.ait.transporte.dto.OrderAssignmentDTO;
import com.ait.transporte.exception.OrderAssignmentNotFoundException;
import com.ait.transporte.model.*;
import com.ait.transporte.repository.AssignmentFileRepository;
import com.ait.transporte.repository.OrderAssignmentRepository;
import com.ait.transporte.service.IDriverService;
import com.ait.transporte.service.IOrderService;
import com.ait.transporte.service.OrderAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderAssignmentServiceImpl implements OrderAssignmentService {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    private final IOrderService orderService;
    private final IDriverService driverService;
    private final OrderAssignmentRepository assignmentRepository;
    private final AssignmentFileRepository fileRepository;

    //Se realizan todas las validaciones para que se respeten las reglas de negocio.
    @Override
    @Transactional
    public OrderAssignmentDTO assignDriver(UUID orderId, UUID driverId) {

        //Se lanza una excepcion si el id del driver no se ha proporcionado
        if (driverId == null) {
            throw new IllegalArgumentException("El ID del conductor es obligatorio");
        }

        //si la orden esta en status distinto al status CREATED, lanza una excepcion
        Order order = orderService.findById(orderId);
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new IllegalStateException("Solo se pueden asignar órdenes en estado CREATED");
        }

        //Valida si la orden existe, porque en ese caso ya ha sido asignado un conductor a esa orden
        if (assignmentRepository.findByOrder_IdOrder(orderId).isPresent()) {
            throw new IllegalStateException("La orden ya tiene un conductor asignado");
        }

        //Valida si el conductor esta en estatus inactive, ya que la regla de negocio menciona
        //que el status de un driver debera ser activo para ser candidado a asignación
        Driver driver = driverService.findById(driverId);
        if (!Boolean.TRUE.equals(driver.getActive())) {
            throw new IllegalStateException("No se puede asignar un conductor inactivo");
        }

        //Se setean loa datos en una instancia del POJO OrderAssignament
        OrderAssignment assignment = new OrderAssignment();
        assignment.setOrder(order);
        assignment.setDriver(driver);
        assignment.setAssignedAt(LocalDateTime.now());
        assignment = assignmentRepository.save(assignment);
        return toDTO(assignment);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderAssignmentDTO getAssignment(UUID orderId) {
        return toDTO(findAssignment(orderId));
    }


    @Override
    @Transactional
    public AssignmentFile addFile(UUID orderId, AssignmentFileType type, MultipartFile upload) throws IOException {
        OrderAssignment assignment = findAssignment(orderId);

        //Valida el formato del archivo y devuelve su tipo MIME
        if (upload == null || upload.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }
        if (upload.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("El archivo no puede superar los 10 MB");
        }

        String fileName = upload.getOriginalFilename();
        String extension = fileName == null ? "" : fileName.substring(fileName.lastIndexOf('.') + 1)
                .toLowerCase(Locale.ROOT);
        byte[] content = upload.getBytes();
        String contentType = validateFile(type, extension, content);

        AssignmentFile file = fileRepository.findByAssignment_IdAndType(assignment.getId(), type)
                .orElseGet(AssignmentFile::new);
        file.setAssignment(assignment);
        file.setType(type);
        file.setFileName(fileName);
        file.setContentType(contentType);
        file.setContent(content);
        return fileRepository.save(file);
    }

    @Override
    @Transactional(readOnly = true)
    public AssignmentFile getFile(UUID orderId, AssignmentFileType type) {
        OrderAssignment assignment = findAssignment(orderId);
        return fileRepository.findByAssignment_IdAndType(assignment.getId(), type)
                .orElseThrow(() -> new OrderAssignmentNotFoundException(
                        "No existe un archivo " + type + " para la orden " + orderId));
    }

    //Valida la asignacion a una orden buscando el id de la orden en la entidad Order
    //si no existe lanza una excepcion
    private OrderAssignment findAssignment(UUID orderId) {
        return assignmentRepository.findByOrder_IdOrder(orderId)
                .orElseThrow(() -> new OrderAssignmentNotFoundException(
                        "La orden " + orderId + " no tiene una asignación"));
    }


    private OrderAssignmentDTO toDTO(OrderAssignment assignment) {
        List<AssignmentFileDTO> files = fileRepository.findAllByAssignment_Id(assignment.getId())
                .stream()
                .map(AssignmentFileDTO::fromEntity)
                .toList();
        return OrderAssignmentDTO.fromEntity(assignment, files);
    }

    //Se validan los dos tipos de archivos: DOCUMENT o PNG o JPG
    private String validateFile(AssignmentFileType type, String extension, byte[] content) {
        return switch (type) {
            case DOCUMENT -> {
                if (!"pdf".equals(extension) || !startsWith(content, "%PDF-".getBytes())) {
                    throw new IllegalArgumentException("El documento debe ser un archivo PDF válido");
                }
                yield "application/pdf";
            }
            case IMAGE -> {
                if ("png".equals(extension) && startsWith(content,
                        new byte[]{(byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A})) {
                    yield "image/png";
                }
                if ("jpg".equals(extension) && content.length >= 3 && content[0] == (byte) 0xFF
                        && content[1] == (byte) 0xD8 && content[2] == (byte) 0xFF) {
                    yield "image/jpeg";
                }
                throw new IllegalArgumentException("La imagen debe ser un archivo PNG o JPG válido");
            }
        };
    }

    private boolean startsWith(byte[] content, byte[] signature) {
        if (content.length < signature.length) {
            return false;
        }
        for (int i = 0; i < signature.length; i++) {
            if (content[i] != signature[i]) {
                return false;
            }
        }
        return true;
    }
}
