package com.ait.transporte.service;

import com.ait.transporte.model.Driver;

import java.util.List;
import java.util.UUID;

public interface IDriverService extends IGenericService<Driver, UUID> {
    List<Driver> findActiveDrivers();
}
