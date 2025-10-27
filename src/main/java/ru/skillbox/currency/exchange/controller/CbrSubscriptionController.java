package ru.skillbox.currency.exchange.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.currency.exchange.service.CbrSubscriptionService;

@RestController
@RequestMapping("api/cbr_list")
@RequiredArgsConstructor
public class CbrSubscriptionController {

    private final CbrSubscriptionService cbrSubscriptionService;

    @GetMapping
    public ResponseEntity<Void> getCbrList(){
        cbrSubscriptionService.refreshCbrCurrencyList();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
