package com.example.cryptoanalizer.clients.external.cmc.model;

import lombok.Data;

import java.util.List;

@Data
public class RequestDto {
    private List<CryptocurrencyDto> data;
}
