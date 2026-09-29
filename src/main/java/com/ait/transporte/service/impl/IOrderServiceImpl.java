package com.ait.transporte.service.impl;

import com.ait.transporte.exception.OrderNotFoundException;
import com.ait.transporte.model.Order;
import com.ait.transporte.model.OrderStatus;
import com.ait.transporte.repository.IGenericRepository;
import com.ait.transporte.repository.IOrderRepository;
import com.ait.transporte.service.IOrderService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IOrderServiceImpl extends CRUDGenericImpl<Order, UUID> implements IOrderService {

    private final IOrderRepository orderRepo;

    @Override
    protected IGenericRepository<Order, UUID> getRepository() {
        return orderRepo;
    }

    // CRUDGenericImpl.findById lanza EstudianteNotFoundException; aquí se mapea al 404 de Curso
    @Override
    public Order findById(UUID id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("ORDER ID NOT FOUND " + id));
    }

    @Override
    public List<Order> findOrders(OrderStatus status, LocalDate createdDate, String origin, String destination) {
        Specification<Order> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (createdDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("createdAt"), createdDate.atStartOfDay()));
                predicates.add(criteriaBuilder.lessThan(
                        root.get("createdAt"), createdDate.plusDays(1).atStartOfDay()));
            }
            if (origin != null && !origin.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("origin")),
                        "%" + origin.trim().toLowerCase(Locale.ROOT) + "%"));
            }
            if (destination != null && !destination.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("destination")),
                        "%" + destination.trim().toLowerCase(Locale.ROOT) + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return orderRepo.findAll(specification);
    }
}