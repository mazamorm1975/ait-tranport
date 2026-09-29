package com.ait.transporte.dto;

import com.ait.transporte.model.Driver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDTO {
    private UUID idDriver;
    private String name;
    private String licenceNumber;
    private Boolean active;

    public static DriverDTO fromEntity(Driver driver) {
        return new DriverDTO(
                driver.getIdDriver(),
                driver.getName(),
                driver.getLicenceNumber(),
                driver.getActive()
        );
    }

    public Driver toEntity() {
        return new Driver(idDriver, name, licenceNumber, active);
    }
}
