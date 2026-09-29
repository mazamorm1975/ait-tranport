package com.ait.transporte.service;

import com.ait.transporte.model.Order;
import com.ait.transporte.model.OrderStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IOrderService extends IGenericService<Order, UUID> {
    List<Order> findOrders(OrderStatus status, LocalDate createdDate, String origin, String destination);
}
