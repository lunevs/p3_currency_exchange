package ru.skillbox.currency.exchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.currency.exchange.model.entity.Currency;

import java.util.Collection;
import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Currency findByIsoNumCode(Long isoNumCode);

    List<Currency> findAllByIsoCodeIn(Collection<String> isoCodes);

}
