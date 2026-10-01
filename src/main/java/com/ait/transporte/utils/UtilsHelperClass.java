package com.ait.transporte.utils;

import com.ait.transporte.config.MapperConfig;
import com.ait.transporte.dto.OrderAssignmentDTO;
import com.ait.transporte.model.Driver;
import com.ait.transporte.model.Order;
import com.ait.transporte.model.OrderAssignment;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UtilsHelperClass {

    private final ModelMapper mapper;

    public OrderAssignmentDTO toDTO(OrderAssignment orderAssignment){
    return mapper.map(orderAssignment, OrderAssignmentDTO.class);
    }

    public OrderAssignment toEntity(OrderAssignmentDTO orderAssignmentDTO){
        return mapper.map(orderAssignmentDTO, OrderAssignment.class);
    }



    public static Order modifyRecordOrderFields(Order order, UUID idOrder) {
        Order modifyOrder = new Order();
        modifyOrder.setIdOrder(idOrder);
        modifyOrder.setDestination(order.getDestination());
        modifyOrder.setStatus(order.getStatus());
        modifyOrder.setOrigin(order.getOrigin());
        modifyOrder.setCreatedAt(order.getCreatedAt());
        modifyOrder.setUpdatedAt(order.getUpdatedAt());
        return modifyOrder;
    }

    public static Order modifyRecordCategoryFields(Order order, UUID idOrder) {
        return modifyRecordOrderFields(order, idOrder);
    }

    public static Driver modifyDriverRecordFields(Driver driver, UUID idDriver) {
        Driver modifyDriverRecordt = new Driver();
        modifyDriverRecordt.setIdDriver(idDriver);
        modifyDriverRecordt.setName(driver.getName());
        modifyDriverRecordt.setLicenceNumber(driver.getLicenceNumber());
        modifyDriverRecordt.setActive(driver.getActive());
        return modifyDriverRecordt;
    }
}