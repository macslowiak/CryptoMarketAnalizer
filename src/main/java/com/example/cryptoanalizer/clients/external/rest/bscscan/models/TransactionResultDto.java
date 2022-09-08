package com.example.cryptoanalizer.clients.external.rest.bscscan.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResultDto {

    @JsonProperty("timeStamp")
    private Long contractCreationDate;
}
