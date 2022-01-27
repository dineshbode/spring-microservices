package com.bode.microservices.currencyexchangeservice.repository;

import com.bode.microservices.currencyexchangeservice.CurrencyExchange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchange, Long> {
    CurrencyExchange findByFromAndTo(String from, String to);
}
