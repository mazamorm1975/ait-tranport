package com.ait.transporte.repository;

import com.ait.transporte.model.Order;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IOrderRepository extends IGenericRepository<Order, UUID>, JpaSpecificationExecutor<Order> {

}
