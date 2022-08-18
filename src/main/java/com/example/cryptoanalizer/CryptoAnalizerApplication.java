package com.example.cryptoanalizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CryptoAnalizerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoAnalizerApplication.class, args);
    }

}
