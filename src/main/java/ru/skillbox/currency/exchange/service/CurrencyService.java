package ru.skillbox.currency.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.currency.exchange.exception.CurrencyNotFoundException;
import ru.skillbox.currency.exchange.model.dto.CurrencyDto;
import ru.skillbox.currency.exchange.model.dto.CurrencyShortViewListDto;
import ru.skillbox.currency.exchange.model.entity.Currency;
import ru.skillbox.currency.exchange.mapper.CurrencyMapper;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyMapper mapper;
    private final CurrencyRepository repository;

    public CurrencyDto getById(Long id) {
        log.debug("CurrencyService: get currency by id {}", id);
        Currency currency = repository.findById(id)
                .orElseThrow(() -> new CurrencyNotFoundException(MessageFormat.format("Currency with id: {0} not found ", id)));
        return mapper.convertToDto(currency);
    }

    public CurrencyDto getByIsoNumCode(Long isoNumCode) {
        log.debug("CurrencyService: get currency by isoNumCode {}", isoNumCode);
        Currency currency = repository.findByIsoNumCode(isoNumCode)
                .orElseThrow(() -> new CurrencyNotFoundException(MessageFormat.format("Currency with isoNumCode: {0} not found ", isoNumCode)));
        return mapper.convertToDto(currency);
    }

    public Double convertValue(Long value, Long numCode) {
        log.debug("CurrencyService: convertValue isoNumCode: {} value: {}", numCode, value);
        CurrencyDto currency = getByIsoNumCode(numCode);
        return value * currency.getValue();
    }

    public CurrencyDto create(CurrencyDto dto) {
        log.debug("CurrencyService: create currency {}", dto.getIsoCode());
        return mapper.convertToDto(repository.save(mapper.convertToEntity(dto)));
    }

    public CurrencyShortViewListDto getShortViewCurrencyList() {
        List<Currency> currencies = repository.findAll();
        log.debug("CurrencyService: getShortViewCurrencyList returned {} currencies", currencies.size());
        return mapper.convertToShortViewListDto(currencies);
    }

    public List<CurrencyDto> getAllCurrencies() {
        List<Currency> currencies = repository.findAll();
        log.debug("CurrencyService: getAll currencies returned {} currencies", currencies.size());
        return currencies.stream().map(mapper::convertToDto).toList();
    }

    public List<CurrencyDto> getAllCurrenciesByIsoCodes(Set<String> isoCodes) {
        List<Currency> currencyList = repository.findAllByIsoCodeIn(isoCodes);
        log.debug("CurrencyService: get {} currencies by filter with {} IsoCodes", currencyList.size(), isoCodes.size());
        return currencyList.stream()
                .map(mapper::convertToDto)
                .toList();
    }

    @Transactional
    public void saveAll(List<CurrencyDto> currencies) {
        log.debug("CurrencyService: save {} currencies", currencies.size());
        repository.saveAll(
            currencies.stream().map(mapper::convertToEntity).toList()
        );
    }

    public Map<Long, CurrencyDto> getAllCurrenciesAsMap() {
        List<CurrencyDto> allCurrencies = getAllCurrencies();

        return allCurrencies.stream()
                .collect(Collectors.toMap(
                        CurrencyDto::getIsoNumCode,
                        currencyDto -> currencyDto));
    }

    public void deleteAllCurrencies() {
        repository.deleteAll();
    }
}
