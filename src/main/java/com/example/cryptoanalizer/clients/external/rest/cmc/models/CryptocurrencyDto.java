package com.example.cryptoanalizer.clients.external.rest.cmc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CryptocurrencyDto {
    private String name;
    private String symbol;
    private PlatformDto platform;

    //todo: DODAC DATE
    private LocalDateTime dateOfFirstHistoricalData;
}