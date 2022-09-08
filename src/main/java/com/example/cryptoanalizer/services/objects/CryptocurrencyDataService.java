package com.example.cryptoanalizer.services.objects;

import com.example.cryptoanalizer.models.Cryptocurrency;
import com.example.cryptoanalizer.models.CryptocurrencyData;

import java.util.List;

public class CryptocurrencyDataService {

    public static boolean isCryptoIdAlreadyInDatabase(List<CryptocurrencyData> data, Cryptocurrency cryptocurrency) {
        return data.stream()
                .anyMatch(singleCryptoData -> singleCryptoData.getId().equals(cryptocurrency.getId()));
    }
}
