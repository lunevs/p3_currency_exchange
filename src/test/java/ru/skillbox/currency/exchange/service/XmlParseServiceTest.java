package ru.skillbox.currency.exchange.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skillbox.currency.exchange.AbstractTest;
import ru.skillbox.currency.exchange.model.xml.CbrCurrencyItem;
import ru.skillbox.currency.exchange.model.xml.CbrCurrencyList;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class XmlParseServiceTest extends AbstractTest {

    @Autowired
    private XmlParseService xmlParseService;

    @Test
    public void whenValidXmlInputThenOk() throws Exception {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("input-data-sample.xml");
        CbrCurrencyList resultList = xmlParseService.parseXml(inputStream);

        assertThat(resultList).isNotNull();
        assertThat(resultList.getCurrencies()).hasSize(3);

        assertThat(resultList.getCurrencies())
                .extracting(CbrCurrencyItem::getIsoCode)
                .containsExactly("AUD", "AZN", "DZD");

        CbrCurrencyItem gbp = resultList.getCurrencies().stream()
                .filter(c -> "DZD".equals(c.getIsoCode()))
                .findFirst()
                .orElseThrow();

        assertThat(gbp.getName()).isEqualTo("Алжирских динаров");
        assertThat(gbp.getNominalValue()).isEqualTo("62,5571");
        assertThat(gbp.getNominal()).isEqualTo(100L);
    }

}
