package com.example.cryptoanalizer.clients.external.rest.cmc.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    private List<CryptocurrencyDto> data;
}
