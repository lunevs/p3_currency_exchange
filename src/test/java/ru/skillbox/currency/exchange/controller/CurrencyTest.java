package ru.skillbox.currency.exchange.controller;

import org.junit.jupiter.api.Test;
import ru.skillbox.currency.exchange.AbstractTest;
import ru.skillbox.currency.exchange.model.dto.CurrencyDto;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class CurrencyTest extends AbstractTest {

    @Test
    public void whenGetCorrectCurrencyThenOk() throws Exception {
        CurrencyDto cur = currencyService.getByIsoNumCode(cUSD.getIsoNumCode());
        mockMvc.perform(get("/api/currency/" + cur.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(cUSD.getName())));
    }

    @Test
    public void whenGetNonExistCurrencyThenNotFound() throws Exception {
        mockMvc.perform(get("/api/currency/" + cZZZ.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", containsString(cZZZ.getId() + " not found")));
    }

    @Test
    public void whenGetAllCurrencyThenOk() throws Exception {
        mockMvc.perform(get("/api/currency"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencies.length()").value(2))
                .andExpect(jsonPath("$.currencies[?(@.name == 'Доллар США')]").exists())
                .andExpect(jsonPath("$.currencies[?(@.name == 'Евро')]").exists());
    }

    @Test
    public void whenConvertExistedCurrencyThenOk() throws Exception {
        Double expectedResult = 10L * cEUR.getValue();
        mockMvc.perform(get("/api/currency/convert")
                        .param("value", String.valueOf(10))
                        .param("numCode", String.valueOf(cEUR.getIsoNumCode())))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expectedResult)));
    }

    @Test
    public void whenConvertNonExistedCurrencyThenNotFound() throws Exception {
        mockMvc.perform(get("/api/currency/convert")
                        .param("value", String.valueOf(10))
                        .param("numCode", String.valueOf(cZZZ.getIsoNumCode())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(404)));
    }


}
