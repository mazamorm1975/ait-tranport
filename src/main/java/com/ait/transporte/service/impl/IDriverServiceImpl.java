package com.ait.transporte.service.impl;

import com.ait.transporte.exception.DriverNotFoundException;
import com.ait.transporte.model.Driver;
import com.ait.transporte.repository.IDriverRepository;
import com.ait.transporte.repository.IGenericRepository;
import com.ait.transporte.service.IDriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IDriverServiceImpl extends CRUDGenericImpl<Driver, UUID> implements IDriverService {

    private final IDriverRepository driverRepo;

    @Override
    protected IGenericRepository<Driver, UUID> getRepository() {
        return driverRepo;
    }

    @Override
    public Driver findById(UUID id) {
        return driverRepo.findById(id)
                .orElseThrow(() -> new DriverNotFoundException("DRIVER ID NOT FOUND " + id));
    }

    @Override
    public List<Driver> findActiveDrivers() {
        return driverRepo.findAllByActiveTrue();
    }
}