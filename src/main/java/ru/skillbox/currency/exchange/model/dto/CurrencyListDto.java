package ru.skillbox.currency.exchange.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyListDto {

    private List<CurrencyItemDto> currencies = new ArrayList<>();

}
