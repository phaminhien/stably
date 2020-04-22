package com.example.stably.controller;

import com.example.stably.entity.Price;
import com.example.stably.service.ConfigService;
import com.example.stably.service.PriceService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import java.io.FileNotFoundException;

@Controller
public class MainController {
    private final PriceService priceService;
    private final ConfigService configService;

    public MainController(PriceService priceService, ConfigService configService) {
        this.priceService = priceService;
        this.configService = configService;
    }

    @GetMapping
    public String index(@RequestParam(defaultValue = "") String symbol,
                        @RequestParam(defaultValue = "5") Integer interval,
                        @RequestParam(defaultValue = "7") Integer duration,
                        @RequestParam(defaultValue = "0") Integer page,
                        @RequestParam(defaultValue = "10") Integer size,
                        Model model) throws FileNotFoundException {
        if (!StringUtils.isEmpty(symbol) && interval > 0 && duration > 0) {
            Page<Price> pricePage = priceService.retrievePriceList(symbol, interval, duration, page, size);
            model.addAttribute("prices", pricePage.getContent());
            model.addAttribute("totalPage", pricePage.getTotalPages());
        } else {
            model.addAttribute("totalPage", 0);
        }
        model.addAttribute("symbol", symbol);
        model.addAttribute("interval", interval);
        model.addAttribute("duration", duration);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("symbols", configService.retrieveSymbols());
        return "index";
    }
}
