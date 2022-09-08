package com.example.cryptoanalizer.clients.external.rest.bscscan.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
public class BscScanClientConfiguration {

    @Bean
    public BscScanRequestInterceptor covalentRequestInterceptor() {
        return new BscScanRequestInterceptor();
    }

}
