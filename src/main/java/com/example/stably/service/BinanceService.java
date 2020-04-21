package com.example.stably.service;

import com.example.stably.dto.MarketDepth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class BinanceService {

    @Value("${config.binance.depthApi}")
    private String depthApi;

    public MarketDepth retrieveMarketDepth(String symbol) {
        RestTemplate restTemplate = new RestTemplate();
        URI depthUri = UriComponentsBuilder.fromUriString(depthApi)
                .queryParam("symbol", symbol)
                .queryParam("limit", 5)
                .build().encode().toUri();
        return restTemplate.getForObject(depthUri, MarketDepth.class);
    }
}
