package com.example.cryptoanalizer.clients.external.rest.cmc.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformDto {
    private String name;
    private String symbol;

    @JsonProperty("token_address")
    private String tokenAddress;
}
