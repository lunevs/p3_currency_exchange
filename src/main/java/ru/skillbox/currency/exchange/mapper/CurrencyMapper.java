package ru.skillbox.currency.exchange.mapper;

import org.mapstruct.Mapper;
import ru.skillbox.currency.exchange.model.dto.CurrencyDto;
import ru.skillbox.currency.exchange.model.dto.CurrencyItemDto;
import ru.skillbox.currency.exchange.model.dto.CurrencyListDto;
import ru.skillbox.currency.exchange.model.entity.Currency;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    CurrencyDto convertToDto(Currency currency);

    Currency convertToEntity(CurrencyDto currencyDto);

    default CurrencyListDto convertToListDto(List<Currency> currencies) {
        return new CurrencyListDto(
                currencies.stream()
                        .map(CurrencyItemDto::from)
                        .toList());
    }
}
