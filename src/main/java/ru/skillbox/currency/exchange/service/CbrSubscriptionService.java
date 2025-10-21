package ru.skillbox.currency.exchange.service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.skillbox.currency.exchange.model.xml.CbrCurrencyList;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class CbrSubscriptionService {

    @Value("${app.subscription.address}")
    private String subscriptionAddress;

    private final CurrencyService currencyService;

    @Scheduled(fixedRateString = "${app.subscription.interval}")
    public void subscribe() {

    }

    public CbrCurrencyList getCbrCurrencyListFromStream() throws JAXBException {


    }

    private HttpURLConnection getConnection(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setRequestProperty("Accept", "application/xml");
        return connection;
    }

    private CbrCurrencyList parseXml(InputStream inputStream) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(CbrCurrencyList.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (CbrCurrencyList) unmarshaller.unmarshal(inputStream);
    }

}
