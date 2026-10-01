package com.ait.transporte.service.impl;

import com.ait.transporte.model.*;
import com.ait.transporte.repository.AssignmentFileRepository;
import com.ait.transporte.repository.OrderAssignmentRepository;
import com.ait.transporte.service.IDriverService;
import com.ait.transporte.service.IOrderService;
import com.ait.transporte.utils.UtilsHelperClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderAssignmentServiceImplTest {

    private IOrderService orderService;
    private IDriverService driverService;
    private OrderAssignmentRepository assignmentRepository;
    private AssignmentFileRepository fileRepository;
    private OrderAssignmentServiceImpl service;

    @BeforeEach
    void setUp() {
        orderService = mock(IOrderService.class);
        driverService = mock(IDriverService.class);
        assignmentRepository = mock(OrderAssignmentRepository.class);
        fileRepository = mock(AssignmentFileRepository.class);
        UtilsHelperClass mapper = mock(UtilsHelperClass.class);
        service = new OrderAssignmentServiceImpl(
                orderService, driverService, assignmentRepository, fileRepository, mapper);
    }

    @Test
    void rejectsOrderThatIsNotCreated() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setStatus(OrderStatus.IN_TRANSIT);
        when(orderService.findById(orderId)).thenReturn(order);

        assertThrows(IllegalStateException.class,
                () -> service.assignDriver(orderId, UUID.randomUUID()));
        verifyNoInteractions(driverService);
        verify(assignmentRepository, never()).save(any());
    }

    @Test
    void rejectsInactiveDriver() {
        UUID orderId = UUID.randomUUID();
        UUID driverId = UUID.randomUUID();
        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        Driver driver = new Driver();
        driver.setActive(false);
        when(orderService.findById(orderId)).thenReturn(order);
        when(assignmentRepository.findByOrder_IdOrder(orderId)).thenReturn(Optional.empty());
        when(driverService.findById(driverId)).thenReturn(driver);

        assertThrows(IllegalStateException.class, () -> service.assignDriver(orderId, driverId));
        verify(assignmentRepository, never()).save(any());
    }

    @Test
    void acceptsPdfAndRejectsMismatchedContent() throws Exception {
        UUID orderId = UUID.randomUUID();
        OrderAssignment assignment = new OrderAssignment();
        assignment.setId(UUID.randomUUID());
        when(assignmentRepository.findByOrder_IdOrder(orderId)).thenReturn(Optional.of(assignment));
        when(fileRepository.findByAssignment_IdAndType(assignment.getId(), AssignmentFileType.DOCUMENT))
                .thenReturn(Optional.empty());
        when(fileRepository.save(any(AssignmentFile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MockMultipartFile validPdf = new MockMultipartFile(
                "file", "proof.pdf", "application/pdf", "%PDF-1.7".getBytes());
        AssignmentFile stored = service.addFile(orderId, AssignmentFileType.DOCUMENT, validPdf);
        assertEquals("application/pdf", stored.getContentType());

        MockMultipartFile invalidPdf = new MockMultipartFile(
                "file", "proof.pdf", "application/pdf", "not a pdf".getBytes());
        assertThrows(IllegalArgumentException.class,
                () -> service.addFile(orderId, AssignmentFileType.DOCUMENT, invalidPdf));
        verify(fileRepository, times(1)).save(any(AssignmentFile.class));
    }

    @Test
    void acceptsPngImage() throws Exception {
        UUID orderId = UUID.randomUUID();
        OrderAssignment assignment = new OrderAssignment();
        assignment.setId(UUID.randomUUID());
        when(assignmentRepository.findByOrder_IdOrder(orderId)).thenReturn(Optional.of(assignment));
        when(fileRepository.findByAssignment_IdAndType(assignment.getId(), AssignmentFileType.IMAGE))
                .thenReturn(Optional.empty());
        when(fileRepository.save(any(AssignmentFile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        byte[] png = {(byte) 0x89, 'P', 'N', 'G', 0x0D, 0x0A, 0x1A, 0x0A};
        MockMultipartFile image = new MockMultipartFile("file", "photo.png", "image/png", png);

        AssignmentFile stored = service.addFile(orderId, AssignmentFileType.IMAGE, image);

        assertEquals("image/png", stored.getContentType());
    }
}
