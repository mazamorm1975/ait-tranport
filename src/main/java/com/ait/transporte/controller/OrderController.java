package com.ait.transporte.controller;

import com.ait.transporte.dto.OrderDTO;
import com.ait.transporte.model.Order;
import com.ait.transporte.model.OrderStatus;
import com.ait.transporte.service.IOrderService;
import com.ait.transporte.utils.UtilsHelperClass;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(OrderDTO.fromEntity(orderService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrders(
            @RequestParam(name = "status", required = false) OrderStatus status,
            @RequestParam(name = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(name = "origin", required = false) String origin,
            @RequestParam(name = "destination", required = false) String destination) {
        List<OrderDTO> orders = orderService.findOrders(status, date, origin, destination)
                .stream()
                .map(OrderDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/generateOrder")
    public ResponseEntity<OrderDTO> generateNewOrder(@RequestBody OrderDTO orderDTO) {
        Order newOrder = orderService.create(orderDTO.toEntity());
        return new ResponseEntity<>(OrderDTO.fromEntity(newOrder), HttpStatus.CREATED);
    }

    @PutMapping("/updateOrder/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@RequestBody OrderDTO orderDTO, @PathVariable("id") UUID id) throws Exception {
        orderService.findById(id);
        Order modifyRecordCategory = UtilsHelperClass.modifyRecordCategoryFields(orderDTO.toEntity(), id);
        Order updatedRecord = orderService.update(modifyRecordCategory, id);
        return ResponseEntity.ok(OrderDTO.fromEntity(updatedRecord));
    }


}
