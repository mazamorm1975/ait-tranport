package com.ait.transporte.repository;

import com.ait.transporte.model.Driver;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IDriverRepository extends IGenericRepository<Driver, UUID> {
    List<Driver> findAllByActiveTrue();
}
