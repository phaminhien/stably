package com.example.stably.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class ConfigService {
    @Value("${config.symbol.path}")
    private String symbolConfig;

    public List<String> retrieveSymbols() throws FileNotFoundException {
        File file = new File(symbolConfig);
        Scanner scanner = new Scanner(file);
        List<String> symbols = new ArrayList<>();
        while (scanner.hasNext()) {
            symbols.add(scanner.next());
        }
        return symbols;
    }
}
