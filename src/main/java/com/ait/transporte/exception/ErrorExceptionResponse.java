package com.ait.transporte.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorExceptionResponse {

    private LocalDateTime timestamp;
    private String description;
    private String path;
}
