package com.example.stably;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StablyApplication {

    public static void main(String[] args) {
        SpringApplication.run(StablyApplication.class, args);
    }

}
