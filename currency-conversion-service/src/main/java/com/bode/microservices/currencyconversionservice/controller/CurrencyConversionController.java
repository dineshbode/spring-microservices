package com.bode.microservices.currencyconversionservice.controller;

import com.bode.microservices.currencyconversionservice.CurrencyConversionBean;
import com.bode.microservices.currencyconversionservice.CurrencyExchangeProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CurrencyConversionController {

    @Autowired
    CurrencyExchangeProxy currencyExchangeProxy;

    @GetMapping("currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrency(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);

        //Calling rest API
        ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class, uriVariables);
        CurrencyConversionBean currencyConversionBean = responseEntity.getBody();
        return new CurrencyConversionBean(currencyConversionBean.getId(),
                from, to, quantity,
                currencyConversionBean.getConversionMultiple(),
                quantity.multiply(currencyConversionBean.getConversionMultiple()),
                currencyConversionBean.getEnvironment());
    }

    @GetMapping("currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity) {
        CurrencyConversionBean currencyConversionBean = currencyExchangeProxy.retrieveExchangeValue(from, to);
        return new CurrencyConversionBean(currencyConversionBean.getId(),
                from, to, quantity,
                currencyConversionBean.getConversionMultiple(),
                quantity.multiply(currencyConversionBean.getConversionMultiple()),
                currencyConversionBean.getEnvironment());
    }
}
