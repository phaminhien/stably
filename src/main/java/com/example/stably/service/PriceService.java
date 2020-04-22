package com.example.stably.service;

import com.example.stably.dto.FormRequest;
import com.example.stably.dto.MarketDepth;
import com.example.stably.entity.Price;
import com.example.stably.repository.PriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PriceService {
    private final PriceRepository priceRepository;
    private final BinanceService binanceService;
    private final ConfigService configService;

    public PriceService(PriceRepository priceRepository, BinanceService binanceService, ConfigService configService) {
        this.priceRepository = priceRepository;
        this.binanceService = binanceService;
        this.configService = configService;
    }

    public List<Price> retrievePriceList(FormRequest request) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusMinutes(request.getInterval());
        LocalDateTime origin = end.minusDays(request.getDuration());
        List<Price> prices = new ArrayList<>();
        while (start.isAfter(origin) || start.isEqual(origin)) {
            Price price = priceRepository.findFirstBySymbolAndCreatedOnBetweenOrderByCreatedOnDesc(request.getSymbol(), start, end);
            if (price != null) prices.add(price);
            end = start;
            start = start.minusMinutes(request.getInterval());
        }
        return prices;
    }

    @Scheduled(fixedRate = 60000)
    public void populateData() throws FileNotFoundException {
        List<String> symbols = configService.retrieveSymbols();
        symbols.forEach(symbol -> {
            MarketDepth marketDepth = binanceService.retrieveMarketDepth(symbol);
            Price price = createPrice(marketDepth);
            price.setSymbol(symbol);
            priceRepository.save(price);
        });
    }

    private Price createPrice(MarketDepth marketDepth) {
        Price price = new Price();
        price.setBid(new BigDecimal(marketDepth.getBids().get(0).get(0)));
        price.setAsk(new BigDecimal(marketDepth.getAsks().get(0).get(0)));
        price.setSpread(price.getAsk().subtract(price.getBid()));
        price.setCreatedOn(LocalDateTime.now());
        return price;
    }
}
