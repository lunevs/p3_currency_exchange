package ru.skillbox.currency.exchange.mapper;

import org.mapstruct.Mapper;
import ru.skillbox.currency.exchange.model.entity.Currency;
import ru.skillbox.currency.exchange.model.xml.CbrCurrencyItem;

@Mapper(componentModel = "spring")
public interface CbrCurrencyMapper {

    default CbrCurrencyItem toCbrCurrencyItem(Currency currency) {
        return CbrCurrencyItem.builder()
                .value(currency.getValue().toString())
                .nominal(currency.getNominal())
                .isoCode(currency.getIsoCode())
                .isoNumCode(currency.getIsoNumCode())
                .name(currency.getName())
                .build();
    }

    default Currency toCurrency(CbrCurrencyItem cbrCurrencyItem) {
        return Currency.builder()
                .value(cbrCurrencyItem.getNominalValueAsDouble())
                .name(cbrCurrencyItem.getName())
                .isoCode(cbrCurrencyItem.getIsoCode())
                .nominal(cbrCurrencyItem.getNominal())
                .isoNumCode(cbrCurrencyItem.getIsoNumCode())
                .build();
    }

}
