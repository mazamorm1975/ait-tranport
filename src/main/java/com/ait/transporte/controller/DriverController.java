package com.ait.transporte.controller;

import com.ait.transporte.dto.DriverDTO;
import com.ait.transporte.model.Driver;
import com.ait.transporte.service.IDriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final IDriverService driverService;

    @PostMapping
    public ResponseEntity<DriverDTO> createDriver(@RequestBody DriverDTO driverDTO) {
        Driver createdDriver = driverService.create(driverDTO.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(DriverDTO.fromEntity(createdDriver));
    }

    @GetMapping("/active")
    public ResponseEntity<List<DriverDTO>> getActiveDrivers() {
        List<DriverDTO> drivers = driverService.findActiveDrivers()
                .stream()
                .map(DriverDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(drivers);
    }
}
