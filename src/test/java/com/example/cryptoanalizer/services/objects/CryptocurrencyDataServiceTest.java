package com.example.cryptoanalizer.services.objects;

import com.example.cryptoanalizer.models.Cryptocurrency;
import com.example.cryptoanalizer.models.CryptocurrencyData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CryptocurrencyDataServiceTest {

    @Test
    void shouldReturnTrueWhenCryptoIdIsInProvidedCryptocurrencyDataList() {
        //given
        List<CryptocurrencyData> cryptocurrencyDataList = new ArrayList<>() {{
            add(CryptocurrencyData.builder()
                    .id(UUID.fromString("9f9cd022-9bd1-4830-8481-2313b4925142"))
                    .build());
        }};
        Cryptocurrency cryptocurrencyToMatch = Cryptocurrency.builder()
                .id(UUID.fromString("9f9cd022-9bd1-4830-8481-2313b4925142")).build();

        //when
        boolean isInDb = CryptocurrencyDataService
                .isCryptoIdAlreadyInProvidedDb(cryptocurrencyDataList, cryptocurrencyToMatch);

        //then
        assertTrue(isInDb);
    }

    @Test
    void shouldReturnFalseWhenCryptoIdIsNotInProvidedCryptocurrencyDataList() {
        //given
        List<CryptocurrencyData> cryptocurrencyDataList = new ArrayList<>() {{
            add(CryptocurrencyData.builder()
                    .id(UUID.fromString("9f9cd022-9bd1-4830-8481-2313b4925142"))
                    .build());
        }};
        Cryptocurrency cryptocurrencyToMatch = Cryptocurrency.builder()
                .id(UUID.fromString("2a76c5cf-2b5d-46e5-a7ce-2ae81e457ba2")).build();

        //when
        boolean isInDb = CryptocurrencyDataService
                .isCryptoIdAlreadyInProvidedDb(cryptocurrencyDataList, cryptocurrencyToMatch);

        //then
        assertFalse(isInDb);
    }
}