package com.example.cryptoanalizer.clients.external.cmc;

import com.example.cryptoanalizer.clients.external.cmc.model.CmcListingStatus;
import com.example.cryptoanalizer.clients.external.cmc.model.CryptocurrencyDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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


    public List<CryptocurrencyDto> getCryptocurrenciesWithStatus(CmcListingStatus cmcListingStatus) {
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

        return requestRepeater(endpoint, requestParams);
    }

    private List<CryptocurrencyDto> requestRepeater(String endpoint, Map<String, String> requestParams) {
        List<CryptocurrencyDto> response = new ArrayList<>();
        boolean isMoreThanRequestSize;
        int iteration = 1;

        do {
            var requestResponse = cmcClient.getAllCryptocurrencies(endpoint, requestParams);
            if (requestResponse.getData().size() >= MAX_REQUEST_SIZE) {
                iteration += MAX_REQUEST_SIZE;
                requestParams.put("start", String.valueOf(iteration));
                isMoreThanRequestSize = true;
                response.addAll(requestResponse.getData());
            } else {
                isMoreThanRequestSize = false;
            }
        } while (isMoreThanRequestSize);

        return response;
    }
}