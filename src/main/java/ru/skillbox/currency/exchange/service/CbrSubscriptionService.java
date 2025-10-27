package ru.skillbox.currency.exchange.service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.skillbox.currency.exchange.mapper.CbrCurrencyMapper;
import ru.skillbox.currency.exchange.mapper.CurrencyMapper;
import ru.skillbox.currency.exchange.model.dto.CurrencyDto;
import ru.skillbox.currency.exchange.model.entity.Currency;
import ru.skillbox.currency.exchange.model.xml.CbrCurrencyItem;
import ru.skillbox.currency.exchange.model.xml.CbrCurrencyList;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class CbrSubscriptionService {

    @Value("${app.subscription.address}")
    private String subscriptionAddress;

    private final CurrencyService currencyService;
    private final CbrCurrencyMapper cbrCurrencyMapper;
    private final CurrencyMapper currencyMapper;
//    private final WebClient cbrLoaderWebClient;

    @Scheduled(fixedRateString = "${app.subscription.interval}")
    public void refreshCbrCurrencyList() {
        log.info("Start loading currency list from CBR at {}", LocalDateTime.now());

//        fetchCurrencyData()
//                .ifPresent(cbrData -> {
//                    if (cbrData.getCurrencies() != null && !cbrData.getCurrencies().isEmpty()) {
//                        processCurrencyData(cbrData.getCurrencies());
//                    } else {
//                        log.warn("Received empty data from CBR");
//                    }
//                });

        try {
            CbrCurrencyList cbrData = getCbrCurrencyListFromStream();
            if (cbrData != null && cbrData.getCurrencies() != null && !cbrData.getCurrencies().isEmpty()) {
                processCurrencyData(cbrData.getCurrencies());
            } else {
                log.warn("Received empty or null data from CBR");
            }
        } catch (Exception e) {
            log.error("Failed to refresh currency data: {}", e.getMessage(), e);
        }
    }

    @Async("currencyLoader")
    public void processCurrencyData(List<CbrCurrencyItem> cbrCurrencyList) {
        log.debug("Processing {} currencies from CBR", cbrCurrencyList.size());

        Map<Long, CurrencyDto> savedCurrenciesMap = getSavedCurrenciesMap();
        List<CurrencyDto> updatedCurrencies = processReceivedCurrencies(cbrCurrencyList, savedCurrenciesMap);

        if (!updatedCurrencies.isEmpty()) {
            currencyService.saveAll(updatedCurrencies);
            log.info("Successfully updated {} currencies", updatedCurrencies.size());
        }
    }

    private CbrCurrencyList getCbrCurrencyListFromStream() {
        try {
            HttpURLConnection connection = getConnection(subscriptionAddress);
            return parseXml(connection.getInputStream());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new CbrCurrencyList();
        }
    }

    private Map<Long, CurrencyDto> getSavedCurrenciesMap() {
        List<CurrencyDto> allCurrencies = currencyService.getAllCurrencies();

        return allCurrencies.stream()
                .collect(Collectors.toMap(
                        CurrencyDto::getIsoNumCode,
                        currencyDto -> currencyDto));
    }

    private List<CurrencyDto> processReceivedCurrencies(List<CbrCurrencyItem> cbrCurrencyList, Map<Long, CurrencyDto> savedCurrenciesMap) {
        log.info("refresh existing currencies and add new currencies");
        List<CurrencyDto> result = new ArrayList<>();
        cbrCurrencyList.forEach(cbrCurrencyItem -> {
            Long isoNumCode = cbrCurrencyItem.getIsoNumCode();

            if (savedCurrenciesMap.containsKey(isoNumCode)) {
                CurrencyDto currentItem = savedCurrenciesMap.get(isoNumCode);
                updateCurrencyData(currentItem, cbrCurrencyItem);
                result.add(currentItem);
                log.debug("Updated currency: {}", isoNumCode);
            } else {
                Currency newCurrency = cbrCurrencyMapper.toCurrency(cbrCurrencyItem);
                CurrencyDto newCurrencyDto = currencyMapper.convertToDto(newCurrency);
                result.add(newCurrencyDto);
                log.debug("Added new currency: {}", isoNumCode);
            }
        });
        return result;
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

    private void updateCurrencyData(CurrencyDto currentItem, CbrCurrencyItem cbrCurrencyItem) {
        currentItem.setNominal(cbrCurrencyItem.getNominal());
        currentItem.setValue(cbrCurrencyItem.getNominalValueAsDouble());
        currentItem.setIsoCode(cbrCurrencyItem.getIsoCode());
        currentItem.setName(cbrCurrencyItem.getName());
    }

//    private Optional<CbrCurrencyList> fetchCurrencyData() {
//        try {
//            CbrCurrencyList cbrCurrencyList = cbrLoaderWebClient.get()
//                    .uri(subscriptionAddress)
//                    .retrieve()
//                    .bodyToMono(CbrCurrencyList.class)
//                    .timeout(Duration.ofSeconds(10))
//                    .doOnSuccess(data -> log.debug("Successfully fetched data from CBR"))
//                    .doOnError(error -> log.error("Error fetching currency data: {}", error.getMessage()))
//                    .block();
//            return Optional.ofNullable(cbrCurrencyList);
//        } catch (WebClientResponseException e) {
//            log.error("HTTP error from CBR: {} - {}", e.getStatusCode(), e.getStatusText());
//        } catch (Exception e) {
//            log.error("Unexpected error fetching data: {}", e.getMessage());
//        }
//        return Optional.empty();
//    }

    private CbrCurrencyList parseXml(InputStream inputStream) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(CbrCurrencyList.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (CbrCurrencyList) unmarshaller.unmarshal(inputStream);
    }

}
