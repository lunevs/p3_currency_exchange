package ru.skillbox.currency.exchange.service;

import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.skillbox.currency.exchange.model.xml.CbrCurrencyList;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionService {

    @Value("${app.subscription.address}")
    private String subscriptionAddress;

    private final XmlParseService xmlParseService;

    public CbrCurrencyList loadCbrData() {
        try {
            HttpURLConnection connection = getConnection(subscriptionAddress);
            return xmlParseService.parseXml(connection.getInputStream());
        } catch (JAXBException e) {
            log.error("CbrSubscriptionService: failed to parse xml from {}", subscriptionAddress, e);
        } catch (Exception e) {
            log.error("CbrSubscriptionService: unexpected error occurred while parsing xml from {}", subscriptionAddress, e);
        }
        return new CbrCurrencyList();
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


}
