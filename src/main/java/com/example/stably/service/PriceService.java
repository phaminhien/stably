package com.example.stably.service;

import com.example.stably.dto.MarketDepth;
import com.example.stably.entity.Price;
import com.example.stably.repository.PriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Page<Price> retrievePriceList(String symbol, Integer interval, Integer duration, Integer page, Integer size) {
        List<LocalDateTime> dateTimes = new ArrayList<>();
        LocalDateTime current = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime start = current.minusDays(duration);
        while (current.isAfter(start) || current.isEqual(start)) {
            dateTimes.add(current);
            current = current.minusMinutes(interval);
        }
        log.info("Retrieving bid ask price history for symbol: {} with interval = {} and duration = {}", symbol, interval, duration);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdOn"));
        return priceRepository.findBySymbolAndCreatedOnIn(symbol, dateTimes, pageable);
    }

    @Scheduled(fixedRate = 60000)
    public void populateData() throws FileNotFoundException {
        List<String> symbols = configService.retrieveSymbols();
        log.info("Saving bid ask price info for list of symbols: {}", symbols.toString());
        symbols.forEach(symbol -> {
            MarketDepth marketDepth = binanceService.retrieveMarketDepth(symbol);
            if (marketDepth.getAsks().size() > 0 || marketDepth.getBids().size() > 0) {
                Price price = createPrice(marketDepth);
                price.setSymbol(symbol);
                Optional<Price> priceOptional = priceRepository.findBySymbolAndCreatedOn(symbol, price.getCreatedOn());
                if (!priceOptional.isPresent()) {
                    priceRepository.save(price);
                }
            }
        });
    }

    private Price createPrice(MarketDepth marketDepth) {
        Price price = new Price();
        price.setBid(new BigDecimal(marketDepth.getBids().get(0).get(0)));
        price.setAsk(new BigDecimal(marketDepth.getAsks().get(0).get(0)));
        price.setCreatedOn(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        return price;
    }
}
