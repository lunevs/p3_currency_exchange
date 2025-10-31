package ru.skillbox.currency.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class CbrSubscriptionService {

    private final CurrencyService currencyService;
    private final CbrCurrencyMapper cbrCurrencyMapper;
    private final CurrencyMapper currencyMapper;
    private final ConnectionService connectionService;

    @Scheduled(fixedRateString = "${app.subscription.interval}")
    public void refreshCbrCurrencyList() {
        log.info("CbrSubscriptionService: loading currency list from CBR at {}", LocalDateTime.now());

        try {
            CbrCurrencyList cbrData = connectionService.loadCbrData();
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
        log.info("CbrSubscriptionService: do processCurrencyData for {} currencies from CBR", cbrCurrencyList.size());

        Map<Long, CurrencyDto> savedCurrenciesMap = currencyService.getAllCurrenciesAsMap();
        List<CurrencyDto> updatedCurrencies = processReceivedCurrencies(cbrCurrencyList, savedCurrenciesMap);

        if (!updatedCurrencies.isEmpty()) {
            currencyService.saveAll(updatedCurrencies);
            log.info("CbrSubscriptionService: successfully updated {} currencies", updatedCurrencies.size());
        }
    }

    private List<CurrencyDto> processReceivedCurrencies(List<CbrCurrencyItem> cbrCurrencyList, Map<Long, CurrencyDto> savedCurrenciesMap) {
        log.info("CbrSubscriptionService: refresh existing currencies and add new currencies");
        List<CurrencyDto> result = new ArrayList<>();
        cbrCurrencyList.forEach(cbrCurrencyItem -> {
            Long isoNumCode = cbrCurrencyItem.getIsoNumCode();

            if (savedCurrenciesMap.containsKey(isoNumCode)) {
                CurrencyDto currentItem = savedCurrenciesMap.get(isoNumCode);
                updateCurrencyData(currentItem, cbrCurrencyItem);
                result.add(currentItem);
                log.debug("CbrSubscriptionService: Updated currency: {}", isoNumCode);
            } else {
                Currency newCurrency = cbrCurrencyMapper.toCurrency(cbrCurrencyItem);
                CurrencyDto newCurrencyDto = currencyMapper.convertToDto(newCurrency);
                result.add(newCurrencyDto);
                log.debug("CbrSubscriptionService: Added new currency: {}", isoNumCode);
            }
        });
        return result;
    }

    private void updateCurrencyData(CurrencyDto currentItem, CbrCurrencyItem cbrCurrencyItem) {
        currentItem.setNominal(cbrCurrencyItem.getNominal());
        currentItem.setValue(cbrCurrencyItem.getNominalValueAsDouble());
        currentItem.setIsoCode(cbrCurrencyItem.getIsoCode());
        currentItem.setName(cbrCurrencyItem.getName());
    }

}
