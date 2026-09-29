package com.ait.transporte.utils;

import com.ait.transporte.model.Driver;
import com.ait.transporte.model.Order;

import java.util.UUID;

public class UtilsHelperClass {

    public static Order modifyRecordCategoryFields(Order order, UUID idOrder){
        Order modifyOrder = new Order();
        modifyOrder.setIdOrder(idOrder);
        modifyOrder.setDestination(order.getDestination());
        modifyOrder.setStatus(order.getStatus());
        modifyOrder.setOrigin(order.getOrigin());
        modifyOrder.setCreatedAt(order.getCreatedAt());
        modifyOrder.setUpdatedAt(order.getUpdatedAt());
        return modifyOrder;
    }

    public static Driver modifyDriverRecordFields(Driver driver, UUID idDriver){
        Driver modifyDriverRecordt = new Driver();
        modifyDriverRecordt.setIdDriver(idDriver);
        modifyDriverRecordt.setName(driver.getName());
        modifyDriverRecordt.setLicenceNumber(driver.getLicenceNumber());
        modifyDriverRecordt.setActive(driver.getActive());
        return modifyDriverRecordt;
    }

}