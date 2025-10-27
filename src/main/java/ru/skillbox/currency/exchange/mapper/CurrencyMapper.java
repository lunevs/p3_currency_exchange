package ru.skillbox.currency.exchange.mapper;

import org.mapstruct.Mapper;
import ru.skillbox.currency.exchange.model.dto.CurrencyDto;
import ru.skillbox.currency.exchange.model.dto.CurrencyShortViewDto;
import ru.skillbox.currency.exchange.model.dto.CurrencyShortViewListDto;
import ru.skillbox.currency.exchange.model.entity.Currency;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    CurrencyDto convertToDto(Currency currency);

    Currency convertToEntity(CurrencyDto currencyDto);

    default CurrencyShortViewListDto convertToShortViewListDto(List<Currency> currencies) {
        return new CurrencyShortViewListDto(
                currencies.stream()
                        .map(CurrencyShortViewDto::from)
                        .toList());
    }
}
