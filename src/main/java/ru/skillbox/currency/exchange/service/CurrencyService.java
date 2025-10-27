package ru.skillbox.currency.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.currency.exchange.model.dto.CurrencyDto;
import ru.skillbox.currency.exchange.model.dto.CurrencyShortViewListDto;
import ru.skillbox.currency.exchange.model.entity.Currency;
import ru.skillbox.currency.exchange.mapper.CurrencyMapper;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyMapper mapper;
    private final CurrencyRepository repository;

    public CurrencyDto getById(Long id) {
        log.info("CurrencyService method getById executed");
        Currency currency = repository.findById(id).orElseThrow(() -> new RuntimeException("Currency not found with id: " + id));
        return mapper.convertToDto(currency);
    }

    public Double convertValue(Long value, Long numCode) {
        log.info("CurrencyService method convertValue executed");
        Currency currency = repository.findByIsoNumCode(numCode);
        return value * currency.getValue();
    }

    public CurrencyDto create(CurrencyDto dto) {
        log.info("CurrencyService method create executed");
        return  mapper.convertToDto(repository.save(mapper.convertToEntity(dto)));
    }

    public CurrencyShortViewListDto getShortViewCurrencyList() {
        log.info("CurrencyService method getShortViewCurrencyList executed");
        List<Currency> currencies = repository.findAll();
        return mapper.convertToShortViewListDto(currencies);
    }

    public List<CurrencyDto> getAllCurrencies() {
        log.info("CurrencyService method getAll executed");
        List<Currency> currencies = repository.findAll();
        return currencies.stream().map(mapper::convertToDto).toList();
    }

    public List<CurrencyDto> getAllCurrenciesByIsoCodes(Set<String> isoCodes) {
        List<Currency> currencyList = repository.findAllByIsoCodeIn(isoCodes);
        log.info("Found {} currency by filter with {} IsoCodes", currencyList.size(), isoCodes.size());
        return currencyList.stream()
                .map(mapper::convertToDto)
                .toList();
    }

    @Transactional
    public void saveAll(List<CurrencyDto> currencies) {
        log.info("CurrencyService method saveAll executed");
        repository.saveAll(
            currencies.stream().map(mapper::convertToEntity).toList()
        );
    }
}
