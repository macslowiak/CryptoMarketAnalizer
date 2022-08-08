package com.example.cryptoanalizer.clients.external.cmc.model;

import lombok.Data;

import java.math.BigInteger;

@Data
public class CryptocurrencyDto {

    private BigInteger id;
    private String name;
    private PlatformDto platform;

}

