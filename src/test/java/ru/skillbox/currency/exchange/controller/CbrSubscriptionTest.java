package ru.skillbox.currency.exchange.controller;

import org.junit.jupiter.api.Test;
import ru.skillbox.currency.exchange.AbstractTest;
import ru.skillbox.currency.exchange.model.dto.CurrencyDto;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CbrSubscriptionTest extends AbstractTest {

    @Test
    public void whenCallToLoadDataThenOk() throws Exception {

        List<CurrencyDto> listBefore = currencyService.getAllCurrencies();

        mockMvc.perform(get("/api/cbr_list"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        List<CurrencyDto> listAfter = currencyService.getAllCurrencies();
        Optional<CurrencyDto> newUSD = listAfter.stream()
                .filter(c -> c.getIsoNumCode().equals(cUSD.getIsoNumCode()))
                .findFirst();
        assertThat(listAfter.size()).isGreaterThan(listBefore.size());
        assertThat(newUSD.isPresent()).isTrue();
        assertThat(newUSD.get().getValue()).isNotEqualTo(cUSD.getIsoNumCode());
        assertThat(listAfter.stream().anyMatch(c -> c.getIsoCode().equals("CNY"))).isTrue();
    }

}
