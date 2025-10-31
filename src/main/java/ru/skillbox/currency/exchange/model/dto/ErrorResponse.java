package ru.skillbox.currency.exchange.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private int code;
    private String message;
    private Instant errorTime;
    private String path;

}