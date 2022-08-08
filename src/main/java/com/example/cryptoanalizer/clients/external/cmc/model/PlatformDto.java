package com.example.cryptoanalizer.clients.external.cmc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PlatformDto {

    private String symbol;

    @JsonProperty("token_address")
    private String tokenAddress;
}
