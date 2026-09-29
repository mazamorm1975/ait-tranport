package com.ait.transporte.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorExceptionResponse> EstudianteHandlerException(OrderNotFoundException ex, WebRequest request){

        ErrorExceptionResponse errorResponseHandler = new ErrorExceptionResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseHandler);
    }

    @ExceptionHandler(DriverNotFoundException.class)
    public ResponseEntity<ErrorExceptionResponse> EstudianteHandlerException(DriverNotFoundException ex, WebRequest request){

        ErrorExceptionResponse errorResponseHandler = new ErrorExceptionResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );

        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseHandler);
    }

    @ExceptionHandler(OrderAssignmentNotFoundException.class)
    public ResponseEntity<ErrorExceptionResponse> orderAssignmentNotFoundHandler(
            OrderAssignmentNotFoundException ex, WebRequest request) {
        ErrorExceptionResponse errorResponse = new ErrorExceptionResponse(
                LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorExceptionResponse> illegalStateHandler(
            IllegalStateException ex, WebRequest request) {
        ErrorExceptionResponse errorResponse = new ErrorExceptionResponse(
                LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorExceptionResponse> illegalArgumentHandler(
            IllegalArgumentException ex, WebRequest request) {
        ErrorExceptionResponse errorResponse = new ErrorExceptionResponse(
                LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
