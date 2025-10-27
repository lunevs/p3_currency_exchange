package ru.skillbox.currency.exchange.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skillbox.currency.exchange.model.entity.Currency;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyShortViewDto {

    private String name;

    private Double value;

    public static CurrencyShortViewDto from(Currency currency) {
        CurrencyShortViewDto dto = new CurrencyShortViewDto();
        dto.setName(currency.getName());
        dto.setValue(currency.getValue());
        return dto;
    }
}
