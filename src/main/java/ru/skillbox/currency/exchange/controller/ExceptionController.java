package ru.skillbox.currency.exchange.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.skillbox.currency.exchange.exception.CurrencyNotFoundException;
import ru.skillbox.currency.exchange.model.dto.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(CurrencyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCurrencyNotFoundException(CurrencyNotFoundException ex, HttpServletRequest request) {
        return defaultHandle(ex.getMessage(), HttpStatus.NOT_FOUND, request.getRequestURI());
    }


    private ResponseEntity<ErrorResponse> defaultHandle(String message, HttpStatus status, String path) {
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                message,
                Instant.now(),
                path);
        return ResponseEntity
                .status(status)
                .body(errorResponse);
    }

}
