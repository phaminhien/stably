package com.example.stably.service;

import com.example.stably.dto.MarketDepth;
import com.example.stably.entity.Price;
import com.example.stably.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;

@Service
public class PriceService {
    private final PriceRepository priceRepository;
    private final BinanceService binanceService;

    @Value("${config.symbol.path}")
    private String symbolConfig;

    public PriceService(PriceRepository priceRepository, BinanceService binanceService) {
        this.priceRepository = priceRepository;
        this.binanceService = binanceService;
    }

    @Scheduled(fixedRate = 60000)
    public void populateData() throws FileNotFoundException {
        File file = new File(symbolConfig);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            String symbol = scanner.next();
            MarketDepth marketDepth = binanceService.retrieveMarketDepth(symbol);
            Price price = createPrice(marketDepth);
            price.setSymbol(symbol);
            priceRepository.save(price);
        }
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
