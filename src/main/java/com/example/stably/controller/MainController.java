package com.example.stably.controller;

import com.example.stably.dto.FormRequest;
import com.example.stably.entity.Price;
import com.example.stably.service.ConfigService;
import com.example.stably.service.PriceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.FileNotFoundException;
import java.util.List;

@Controller
public class MainController {
    private final PriceService priceService;
    private final ConfigService configService;

    public MainController(PriceService priceService, ConfigService configService) {
        this.priceService = priceService;
        this.configService = configService;
    }

    @GetMapping
    public String index(Model model) throws FileNotFoundException {
        FormRequest request = new FormRequest();
        request.setInterval(5);
        request.setDuration(7);
        model.addAttribute("request", request);
        model.addAttribute("symbols", configService.retrieveSymbols());
        return "index";
    }

    @PostMapping
    public String submit(@ModelAttribute FormRequest request, Model model) throws FileNotFoundException {
        List<Price> prices = priceService.retrievePriceList(request);
        model.addAttribute("prices", prices);
        model.addAttribute("request", request);
        model.addAttribute("symbols", configService.retrieveSymbols());
        return "index";
    }
}
