package com.example.cryptoanalizer.clients.external.cmc;

import com.example.cryptoanalizer.clients.external.cmc.model.CmcListingStatus;
import com.example.cryptoanalizer.clients.external.cmc.model.CryptocurrencyDto;
import com.example.cryptoanalizer.models.Blockchain;
import com.example.cryptoanalizer.models.Cryptocurrency;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.cryptoanalizer.clients.external.cmc.CmcClientConfiguration.MAX_REQUEST_SIZE;

@Component
public class CryptoCmcClientProvider {
    private final CmcClient cmcClient;

    public CryptoCmcClientProvider(CmcClient cmcClient) {
        this.cmcClient = cmcClient;
    }


    public List<Cryptocurrency> getCryptocurrenciesWithStatus(CmcListingStatus cmcListingStatus) {
        String endpoint = "/v1/cryptocurrency/map";
        String listingStatus;

        switch (cmcListingStatus) {
            case ACTIVE -> listingStatus = "active";
            case INACTIVE -> listingStatus = "inactive";
            case UNTRACKED -> listingStatus = "untracked";
            default -> throw new IllegalStateException("Unexpected value: " + cmcListingStatus);
        }

        Map<String, String> requestParams = new HashMap<>() {{
            put("listing_status", listingStatus);
            put("start", "1");
            put("limit", String.valueOf(MAX_REQUEST_SIZE));
        }};

        return createCryptocurrencyFrom(
                requestRepeater(endpoint, requestParams)
        );
    }

    public List<Cryptocurrency> createCryptocurrencyFrom(List<CryptocurrencyDto> cryptocurrencyDtoList) {
        List<Cryptocurrency> cryptocurrencies = new ArrayList<>();
        if (cryptocurrencyDtoList == null || cryptocurrencyDtoList.equals(Collections.emptyList())) {
            return cryptocurrencies;
        }
        cryptocurrencyDtoList.forEach(cryptocurrencyDto ->
                {
                    var cryptocurrency = Cryptocurrency.builder()
                            .name(cryptocurrencyDto.getName())
                            .symbol(cryptocurrencyDto.getSymbol())
                            .build();
                    if (cryptocurrencyDto.getPlatform() != null) {
                        var blockchain = Blockchain.builder()
                                .name(cryptocurrencyDto.getPlatform().getName())
                                .symbol(cryptocurrencyDto.getPlatform().getSymbol()).build();
                        cryptocurrency.setBlockchain(blockchain);
                        cryptocurrency.setAddress(cryptocurrencyDto.getPlatform().getTokenAddress());
                    }
                    cryptocurrencies.add(cryptocurrency);
                }
        );
        return cryptocurrencies;
    }

    private List<CryptocurrencyDto> requestRepeater(String endpoint, Map<String, String> requestParams) {
        List<CryptocurrencyDto> response = new ArrayList<>();
        boolean isMoreThanRequestSize;
        int iteration = 1;

        do {
            var requestResponse = cmcClient.getAllCryptocurrencies(endpoint, requestParams);
            iteration += MAX_REQUEST_SIZE;
            requestParams.put("start", String.valueOf(iteration));
            response.addAll(requestResponse.getData());
            isMoreThanRequestSize = requestResponse.getData().size() >= MAX_REQUEST_SIZE;
        } while (isMoreThanRequestSize);

        return response;
    }
}