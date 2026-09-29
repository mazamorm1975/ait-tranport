package com.ait.transporte.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID idDriver;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "licenceNumber", nullable = false)
    private String licenceNumber;

    @Column(name = "active", nullable = false)
    private Boolean active;

}
