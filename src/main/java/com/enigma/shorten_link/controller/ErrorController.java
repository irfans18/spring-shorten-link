package com.enigma.shorten_link.controller;

import com.enigma.shorten_link.model.base.CommonResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<CommonResponse<?>> handleResponseStatusException(ResponseStatusException e) {
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(e.getStatusCode().value())
                .message(e.getReason())
                .build();
        return ResponseEntity
                .status(e.getStatusCode())
                .body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonResponse<?>> handleConstraintViolationException(ConstraintViolationException e) {
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CommonResponse<?>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        CommonResponse.CommonResponseBuilder<Object> builder = CommonResponse.<Object>builder();
        HttpStatus httpStatus;
        if (e.getMessage().contains("foreign key constraint ")) {
            builder.statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("failed to delete data because it is used in another table");
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (e.getMessage().contains("unique constraint")) {
            builder.statusCode(HttpStatus.CONFLICT.value())
                    .message("failed to insert data because it already exists");
            httpStatus = HttpStatus.CONFLICT;
        }else {
            builder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message("Internal Server Error");
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        }

        return ResponseEntity
                .status(httpStatus)
                .body(builder.build());
    }
}
